package com.github.walterfan.hello;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class MainApp
{


    public static void main(String[] args) throws IOException
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        ctx.getEnvironment().getPropertySources().addFirst(
                new ResourcePropertySource("classpath:application.properties"));
        ctx.register(MainConfig.class);
        ctx.refresh();
        Potato p = ctx.getBean(Potato.class);

        System.out.println("Potato Name: " + p.getName());
        System.out.println("App Id: " + ctx.getEnvironment().getProperty("app.id"));
        ctx.close();

    }
}
