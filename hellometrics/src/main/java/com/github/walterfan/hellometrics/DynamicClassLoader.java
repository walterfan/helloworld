package com.github.walterfan.hellometrics;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class DynamicClassLoader extends ClassLoader {

    private Map<String, String> classFileMap = new HashMap<>();

    public DynamicClassLoader() {
        super(ClassLoader.getSystemClassLoader());
    }


    public File getObjFile(String name) {
        String filePath = classFileMap.get(name);
        return new File(filePath);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Class clazz = null;
        try {
            byte[] data = getClassFileBytes(getObjFile(name));
            clazz = defineClass(name, data, 0, data.length);
            if (null == clazz) {
                log.warn("class {} not found", name);
            }
        } catch (Exception e) {
            log.error("load class error", e);
        }
        return clazz;

    }


    private byte[] getClassFileBytes(File file) throws Exception {
        try(FileInputStream fis = new FileInputStream(file)) {
            return IOUtils.toByteArray(fis);
        }
    }


}
