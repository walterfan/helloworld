package com.github.walterfan.hellometrics;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class ImageStore {

    private RemovalListener<String, Image> removalListener;
    private CacheLoader<String, Image> cacheLoader;
    private LoadingCache<String, Image> imageCache;

    public void init() {

        imageCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .removalListener(removalListener)
                .build(cacheLoader);
    }

    public long getEvictionsCount() {
        return this.imageCache.stats().evictionCount();
    }
}
