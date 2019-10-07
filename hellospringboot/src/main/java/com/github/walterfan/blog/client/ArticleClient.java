package com.github.walterfan.blog.client;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.walterfan.blog.entity.Article;

import com.github.walterfan.blog.entity.Category;
import com.github.walterfan.blog.util.JsonWrapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by yafan on 17/12/2017.
 */
@Slf4j
public class ArticleClient {

    private final String apiUrl;

    private static ObjectMapper mapper = new ObjectMapper();

    private OkHttpClient httpClient;

    private MediaType jsonMediaType = okhttp3.MediaType.parse("application/json");

    public ArticleClient(String apiUrl) {
        this.apiUrl = apiUrl;
        httpClient = new OkHttpClient();
    }

    public Article createArticle(Article article) throws IOException {


        String postBody = JsonWrapper.getInstance().toString(article);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(jsonMediaType, postBody))
                .build();


        log.info("createArticle {}, {} ", apiUrl, postBody);
        try (Response response = httpClient.newCall(request).execute()) {

            if(response.isSuccessful()) {
                String body = response.body().string();
                log.info("createArticle body: {}" , body);
                Article articleRet = JsonWrapper.getInstance().fromJson(body, Article.typeRef());
                return articleRet;
            }
            throw new IOException("createArticle error: " + response);
        }

    }

    public Article updateArticle(Article article) throws IOException {
        String putBody = JsonWrapper.getInstance().toString(article);

        Request request = new Request.Builder()
                .url(apiUrl)
                .put(RequestBody.create(jsonMediaType, putBody))
                .build();

        log.info("updateArticle {}, {} ", apiUrl, putBody);
        try (Response response = httpClient.newCall(request).execute()) {

            if(response.isSuccessful()) {
                String body = response.body().string();
                log.info("updateArticle body: {}" , body);
                Article articleRet = JsonWrapper.getInstance().fromJson(body, Article.typeRef());
                return articleRet;
            }
            throw new IOException("createArticle error: " + response);
        }
    }

    public Article readArticle(String articleId)  throws IOException {
        String strUrl = apiUrl + "/" + articleId;
        Request request = new Request.Builder()
                .url(strUrl)
                .get()
                .build();

        log.info("readArticle {} ", strUrl);
        try (Response response = httpClient.newCall(request).execute()) {

            if(response.isSuccessful()) {
                String body = response.body().string();
                log.info("readArticle body: {}" , body);
                Article articleRet = JsonWrapper.getInstance().fromJson(body, Article.typeRef());
                return articleRet;
            }
            throw new IOException("createArticle error: " + response);
        }
    }

    public List<Article> queryArticle()  throws IOException {

        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .build();

        log.info("queryArticle {} ", apiUrl);
        try (Response response = httpClient.newCall(request).execute()) {

            if(response.isSuccessful()) {
                String body = response.body().string();
                log.info("readArticle body: {}" , body);
                List<Article> articleRets = JsonWrapper.getInstance().fromJson(body, Article.listTypeRef());
                return articleRets;
            }
            throw new IOException("createArticle error: " + response);
        }
    }


    public void deleteArticle(String articleId)  throws IOException {
        String strUrl = apiUrl + "/" + articleId;
        Request request = new Request.Builder()
                .url(strUrl)
                .delete()
                .build();
        log.info("deleteArticle {} ", strUrl);
        try (Response response = httpClient.newCall(request).execute()) {

            if(!response.isSuccessful()) {
                throw new IOException("deleteArticle error: " + response.code());
            }

        }
    }

    public static void main(String[] args) throws ParseException, IOException {

        ArticleClient articleClient = new ArticleClient("http://localhost:8090/api/v1/articles");
        if(args.length == 0) {
            Article article = Article.builder()
                    .id(UUID.randomUUID().toString())
                    .title("test title")
                    .content("test content")
                    .build();

            log.info("create {} " , article.toString());
            articleClient.createArticle(article);


            Article articleRet = articleClient.readArticle(article.getId());
            log.info("read {} " , articleRet.toString());


            articleClient.deleteArticle(article.getId());
            log.info("delete {}",  article.getId());


            List<Article> list = articleClient.queryArticle();
            log.info("list {}",  list);
        }



    }
}
