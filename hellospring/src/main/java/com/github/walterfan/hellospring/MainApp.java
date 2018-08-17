package com.github.walterfan.hellospring;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;


@Slf4j
public class MainApp
{

    @Autowired
    private FileService fileService;

    public static void main(String[] args) throws IOException
    {
        try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {

            ctx.getEnvironment().getPropertySources().addFirst(
                    new ResourcePropertySource("classpath:application.properties"));


            ctx.register(LogBeanPostProcessor.class);
            ctx.register(MainConfig.class);
            ctx.refresh();

            MainApp app = ctx.getBean(MainApp.class);

            Potato p1 = ctx.getBean(Potato.class, "sleep");


            log.info("Potato1: {}" , p1);


            Function<String, Potato> factory = (Function<String, Potato> )ctx.getBean("potatoFactory", "sleep");
            Potato p2 = factory.apply("read");

            log.info("Potato2: {}" , p2);
            log.info("App Id: {}" , ctx.getEnvironment().getProperty("app.id"));

            app.listFiles(".", ".java");
        }


    }

    public void listFiles(String dirName, String fixExt) {
        try {
            System.out.println("-- list files --");
            List<Path> files = fileService.getFiles(dirName, fixExt);
            files.forEach(System.out::println);
        } catch (IOException e) {
            log.error("listFiles error", e);
        }

    }

    @Override
    public String toString() {
        return "MainApp { fileService=" + fileService + '}';
    }
}
