package com.github.walterfan.helloconcurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

public class WordNumbers {

    public static final Map<Character, String> number2letters = ImmutableMap.<Character, String>builder()
            .put('1', " ")
            .put('2', "abc")
            .put('3', "def")
            .put('4', "ghi")
            .put('5', "jkl")
            .put('6', "mno")
            .put('7', "pqrs")
            .put('8', "tuv")
            .put('9', "wxyz")
            .put('0', "-")
            .build();

    private List<Character> stringToList(String str) {
        int size = str.length();
        if(size == 0 ) {
            return Collections.emptyList();
        }
        List<Character> aList = new ArrayList<>(size);
        for(int i=0; i < size; ++i) {
            char ch = str.charAt(i);
            aList.add(ch);
        }
        return aList;
    }

    public List<String> findLetters(String numbers) {
        List<Character> numberList = stringToList(numbers);
        int size = numberList.size();
        if(size == 0 ) {
            return Collections.emptyList();
        }
        List<String> lettersList = new ArrayList<>(size);
        for(Character ch: numberList) {

            String letters = number2letters.get(ch);
            lettersList.add(letters);
        }

        return Collections.emptyList();
    }

    public List<String> findWords(List<String> lettersList) {
        List<List<Character>> results = new ArrayList<>();
        int size = lettersList.size();
        for(int i=0; i < size; ++i) {
            String letters = lettersList.get(i);
            List<Character> letterList = stringToList(letters);

            for(int j = i + 1; j < size; ++j) {

            }
        }
        return Collections.emptyList();
    }
}
