package com.github.walterfan.hellometrics;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javassist.CannotCompileException;
import javassist.ClassPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: Walter Fan
 * @Date: 22/1/2020, Wed
 **/
@Slf4j
public class OomDemo {
    private static ClassPool classPool = ClassPool.getDefault();
    private int totalCount = 0;
    //key: language, value: countries
    private Multimap<String, String>  localeMultiMap = ArrayListMultimap.create();
    private Map<String, Set<String>> langCountriesMap = new HashMap<>();
    private final String demoType;
    private final int loopCount;

    public OomDemo(String demoType, int loopCount) {
        this.demoType = demoType;
        this.loopCount = loopCount;
    }

    public void addLocale(String key, String value) {

        if(Strings.isBlank(key) || Strings.isBlank(value)) {
            return;
        }
        totalCount++;
        log.debug("addLocale {} -> {}", key, value);
        if("multimap".equals(demoType)) {
            this.localeMultiMap.put(key, value);
        } else {
            Set<String> countries = langCountriesMap.get(key);
            if(null == countries) {
                countries = new HashSet<>();
                langCountriesMap.put(key, countries);
            }
            countries.add(value);
        }


    }
    // java.lang.OutOfMemoryError:Java heap space
    public void noHeapSpace() {
        Integer[] array = new Integer[this.loopCount];
    }

    //java.lang.OutOfMemoryError: Metaspace or PermSpace
    public void noMetaSpace() throws CannotCompileException {
        for (int i = 0; i < this.loopCount; i++) {
            Class c = classPool.makeClass("com.github.walterfan.hellometrics.OomDemo" + i).toClass();
            log.info("{}. makeClass {}", i, c.getName());
        }
    }


    // java.lang.OutOfMemoryError: unable to create new native thread
    public void noNativeMemory() {
        for (int i = 0; i < this.loopCount; ++i) {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000000);
                    } catch(InterruptedException e) { }
                }
            }.start();
            log.info("Thread " + i + " created");
        }
    }


    public void run() {
        log.info("--- run demo {} ---", this.demoType);
        try {
            switch (this.demoType) {
                case "heap":
                    noHeapSpace();
                    break;
                case "native":
                    noNativeMemory();
                    break;
                case "meta":
                    noMetaSpace();
                    break;
                default:
                    noHeapDemo();

            }
        } catch(Exception e) {
            log.error("run error of " + this.demoType + ", " + this.loopCount, e);
        }
    }
    // java.lang.OutOfMemoryError: Java heap space
    private void noHeapDemo() {
        for(long i =0; i < loopCount; ++i) {
            for(Locale locale: Locale.getAvailableLocales()) {
                this.addLocale(locale.getLanguage(), locale.getCountry());
                log.debug("{}/{}. {}", i, totalCount, locale);
            }
        }
        wait4Input(totalCount + " added, exit?");
        printLocales();
    }

    public static String wait4Input(String prompt) {
        System.out.print(prompt);
        System.out.flush();

        try (InputStreamReader is = new InputStreamReader(System.in)) {
            BufferedReader in = new BufferedReader(is);
            return in.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }

    public void printLocales() {
        final AtomicInteger i = new AtomicInteger(0);

        if("multimap".equals(demoType)) {
            localeMultiMap.keySet().stream().sorted().forEach(key -> {
                log.info("{}. language: {}, countries: {} ", i.incrementAndGet(),
                        key,
                        localeMultiMap.get(key).stream().collect(Collectors.joining(",")));
            });
        } else {

            langCountriesMap.keySet().stream().sorted().forEach(key -> {
                log.info("{}. language: {}, countries: {} ", i.incrementAndGet(),
                        key,
                        langCountriesMap.get(key).stream().collect(Collectors.joining(",")));
            });
        }


    }

    public static void launchTool(String type, String count)  {
        int loopCount = NumberUtils.toInt(count, 1);
        OomDemo demo = new OomDemo(type, loopCount);
        demo.run();
        Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(String.format("Used memory: {}m", (memory/1024/1024)));
    }

    public static void main(String[] args) {
        Options options = new Options();

        Option opt = new Option("t", "type", true, "demo type: " +
                "heap|native|meta|multimap|hashmap");
        options.addOption(opt);

        opt = new Option("c", "count", true, "loop count");
        options.addOption(opt);

        opt = new Option("h", "help", false, "optional, print help");
        opt.setRequired(false);
        options.addOption(opt);

        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLine commandLine = null;
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                hf.printHelp("OomDemo", options, true);
                return;
            }

            launchTool(commandLine.getOptionValue("t"), commandLine.getOptionValue("c"));

        } catch (Exception e) {
            log.error("launch error: ", e);
            hf.printHelp("helper", options, true);
        }

    }

}
