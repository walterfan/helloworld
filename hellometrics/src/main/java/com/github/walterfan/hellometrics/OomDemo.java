package com.github.walterfan.hellometrics;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Walter Fan
 * @Date: 22/1/2020, Wed
 **/
@Slf4j
public class OomDemo {
    private Multimap<String, String>  localeMultiMap = ArrayListMultimap.create();
    private Map<String, String>  localeMap = new HashMap<>();
    
    public void addLocale(String key, String value) {
            this.localeMultiMap.put(key, value);
            this.localeMap.put(key, value);
    }


    private  void run() {
        long count = 0;
        log.info("--- start OomDemo ---");
        String[] locales = Locale.getISOCountries();
        while(true) {
            try {
                for(Locale locale: Locale.getAvailableLocales()) {
                    this.addLocale(locale.getLanguage(), locale.getCountry());
                }

            } catch (Exception e) {
                log.info("localeMultiMap size is {}", localeMultiMap.size());
                log.info("localeMap size is {}", localeMap.size());
                log.error("addLocale error" + count, e);

                break;
            }
        }
        Uninterruptibles.sleepUninterruptibly(60, TimeUnit.MINUTES);
    }

    public static void main(String[] args) {
        OomDemo demo = new OomDemo();
        demo.run();
    }

}
