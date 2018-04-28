package com.github.walterfan.blog.entity;

import com.github.walterfan.blog.util.JsonWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by yafan on 19/12/2017.
 */
@Slf4j
public class ArticleTest {
    @Test
    public void testFromJson() throws IOException {
        String jsonStr = loadJson("article.json");
        Article article = JsonWrapper.getInstance().fromJson(jsonStr, Article.typeRef());

        assertEquals("test title", article.getTitle());
    }

    private static String loadJson(String path) throws IOException {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                log.error("open error of " + path);
            }

            return IOUtils.toString(in);
        }


    }

}
