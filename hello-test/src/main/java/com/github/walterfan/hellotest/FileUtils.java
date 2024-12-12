package com.github.walterfan.hellotest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yafan on 24/1/2018.
 */
@Slf4j
public class FileUtils {

    public static final FileFilter javaFileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {

            if(file.isDirectory()) {
                return true;
            }
            if(file.getName().endsWith(".java")) {
                return true;
            }

            return false;
        }
    };

    public static List<String> listFiles(File folder, FileFilter filter) {
        List<String> files = new ArrayList<>();
        listDir(new File("."), files, filter);
        return files;
    }

    public static void listDir(File folder, List<String> fileNames, FileFilter filter) {
        File[] files = folder.listFiles(filter);
        for (File file: files) {
            if(file.isFile()) {
                fileNames.add(file.getName());
            } else if (file.isDirectory()) {
                listDir(file, fileNames, filter);
            }
        }
    }






}
