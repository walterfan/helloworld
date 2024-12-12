package com.github.walterfan.hello.annotation;


import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.github.walterfan.hello.annotation.TestCase")
@AutoService(Processor.class)
public class TestCaseProcessor extends AbstractProcessor {
    public final static String TABLE_TITLE1 = "| # | feature | case | scenario | given | when | then |\n";
    public final static String TABLE_TITLE2 = "|---|---|---|---|---|---|---|\n";
    public final static String TABLE_ROW = "| %d | %s | %s | %s | %s | %s | %s |\n";

    private File testcaseFile = new File("./TestCases.md");
    private StringBuilder testcaseBuilder = new StringBuilder();
    private AtomicInteger testCaseNum = new AtomicInteger(0);

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        testcaseBuilder.append("# Testcases");
        testcaseBuilder.append("\n");
        testcaseBuilder.append(TABLE_TITLE1);
        testcaseBuilder.append(TABLE_TITLE2);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(testcaseFile))) {
            bw.write(testcaseBuilder.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        StringBuilder sb = new StringBuilder();
        for (TypeElement annotation : annotations) {

            for (Element element : roundEnvironment.getElementsAnnotatedWith(annotation)) {

                TestCase testCase = element.getAnnotation(TestCase.class);

                if (testCase != null) {
                    String line = String.format(TABLE_ROW, testCaseNum.incrementAndGet(), testCase.feature(), testCase.value(), testCase.scenario(), testCase.given(), testCase.when(), testCase.then());
                    sb.append(line);

                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(testcaseFile, true))) {
            bw.write(sb.toString());
            System.out.println("testcases:\n" + sb.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
