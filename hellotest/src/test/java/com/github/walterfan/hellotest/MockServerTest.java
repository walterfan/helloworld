package com.github.walterfan.hellotest;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockserver.model.HttpResponse.response;
import static org.testng.Assert.assertTrue;

/**
 * Created by yafan on 21/11/2017.
 */
@Slf4j
public class MockServerTest {

    public static final String ACCESS_TOKEN_URL = "/oauth2/api/v1/access_token";

    public static final String ACCESS_TOKEN_RESP = "{ \"token\": \"abcd1234\"}";

    private int listenPort;

    private OkHttpClient httpClient;

    private ClientAndServer mocker;


    public MockServerTest() {
        listenPort = 10086;
        httpClient = new OkHttpClient();
    }

    @BeforeSuite
    public void startup() {
        mocker = ClientAndServer.startClientAndServer(listenPort);
    }


    @AfterSuite
    public void shutdown() {
        mocker.stop(true);
    }

    @Test
    public void testGet() throws IOException {

        HttpRequest mockReq = new HttpRequest().withMethod("GET").withPath(ACCESS_TOKEN_URL);
        HttpResponse mockResp = new HttpResponse().withStatusCode(200).withBody(ACCESS_TOKEN_RESP).withHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        mocker.when(mockReq, Times.exactly(1))
              .respond(mockResp);

        String theUrl = String.format("http://localhost:%d%s?%s" , listenPort, ACCESS_TOKEN_URL, "client_id=test&client_secret=pass");
        Request request = new Request.Builder()
                .url(theUrl)
                .build();

        Response response = httpClient.newCall(request).execute();
        assertTrue(response.isSuccessful());


        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            log.info(responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }

        String strResult = response.body().string();
        log.info(" strResult: {}", strResult);
        assertEquals(strResult, ACCESS_TOKEN_RESP);

        mocker.verify(mockReq);
    }

    @Test
    public void testPost() throws IOException {


        String requestBody="{\"token\": \"abc\"}";
        String responseBody = "{\"result\": \"okay\"}";

        HttpRequest mockReq = new HttpRequest()
                .withMethod("POST")
                .withPath(ACCESS_TOKEN_URL)
                .withBody(requestBody);



        HttpResponse mockResp = new HttpResponse()
                .withStatusCode(200)
                .withHeader( new Header("Content-Type", "application/json; charset=utf-8"))
                .withBody(responseBody);

        mocker.when(mockReq, Times.once()).respond(mockResp);

        String theUrl = String.format("http://localhost:%d%s" , listenPort, ACCESS_TOKEN_URL);
        Request request = new Request.Builder()
                .url(theUrl)
                .method("POST", RequestBody.create(MediaType.parse("application/json"),requestBody ))
                .build();
        Response response = httpClient.newCall(request).execute();
        assertTrue(response.isSuccessful());
        assertEquals(response.body().string(), responseBody);
        mocker.verify(mockReq);
    }


}
