package com.github.walterfan.hellotest;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by yafan on 23/1/2018.
 */
@Slf4j
public class LoadingCacheTest {
    private LoadingCache<String,  String> internalCache;

    @Mock
    private CacheLoader<String, String> cacheLoader;

    @Mock
    private RemovalListener<String, String> cacheListener;

    @Captor
    private ArgumentCaptor<RemovalNotification<String, String>> argumentCaptor;

    private Answer<String> loaderAnswer;

    private AtomicInteger loadCounter = new AtomicInteger(0);

    @BeforeMethod
    public void setup() {

        MockitoAnnotations.initMocks(this);

        this.internalCache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .removalListener(this.cacheListener)
                .build(this.cacheLoader);

        this.loaderAnswer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                String key = invocationOnMock.getArgumentAt(0, String.class);
                switch(loadCounter.getAndIncrement()) {
                    case 0:
                        return "alice";
                    case 1:
                        return "bob";
                    case 2:
                        return "carl";
                    default:
                        return "unknown";
                }
            }
        };
    }

    @Test
    public void cacheTest() throws Exception {
        //Mock the return value of loader
        //Mockito.when(cacheLoader.load(Mockito.anyString())).thenReturn("alice");
        Mockito.when(cacheLoader.load(Mockito.anyString())).thenAnswer(loaderAnswer);

        assertTrue("alice".equals(internalCache.get("name")));

        //sleep for 2 seconds
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        assertTrue("bob".equals(internalCache.get("name")));

        verify(cacheLoader, times(2)).load("name");
        verify(cacheListener).onRemoval(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue().getKey(), "name");
        assertEquals(argumentCaptor.getValue().getValue(), "alice");
        assertEquals(argumentCaptor.getValue().getCause(), RemovalCause.EXPIRED);
    }
}
