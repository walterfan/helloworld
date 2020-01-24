package com.github.walterfan.hellometrics;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class ImageStore {

    private RemovalListener<String, Image> removalListener;
    private CacheLoader<String, Image> cacheLoader;
    private LoadingCache<String, Image> imageCache;
    private static ImmutableSet<String> imageSuffixSet = ImmutableSet.of(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");

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

    public static boolean isImageFile(Path path) {
        if (path != null && Files.isRegularFile(path) && Files.exists(path)) {

            for (String suffix : imageSuffixSet) {
                if (path.toString().endsWith(suffix)) {
                    log.info("path: {} contains {}", path, suffix);
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) throws IOException {
        String rootDir = "/workspace/walter/wfnote";

        Files.walk(Paths.get(rootDir))
                .filter(ImageStore::isImageFile)
                .forEach(System.out::println);


    }

}
