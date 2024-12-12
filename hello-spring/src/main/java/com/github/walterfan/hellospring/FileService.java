package com.github.walterfan.hellospring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FileService {

    public List<Path> getFiles(String dirName, String fileExt) throws IOException {
        Path filePath = Paths.get(dirName);
        return Files.walk(filePath)
                .filter(s -> s.toString().endsWith(fileExt))
                .map(Path::getFileName)
                .sorted()
                .collect(toList());
    }

    @Override
    public String toString() {
        return "FileService";
    }

    @PostConstruct
    public void setup() {
        log.info("FileService setup");
    }

    @PreDestroy
    public void teardown() {
        log.info("FileService teardown");
    }
}
