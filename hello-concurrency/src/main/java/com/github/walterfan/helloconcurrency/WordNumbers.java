package com.github.walterfan.helloconcurrency;

import java.util.*;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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


    public List<String> findLetters(String numbers) {
        char[] arrNumber = numbers.toCharArray();
        int size = arrNumber.length;
        if (size == 0) {
            return Collections.emptyList();
        }
        List<String> lettersList = new ArrayList<>(size);
        for (Character ch : arrNumber) {

            String letters = number2letters.get(ch);
            lettersList.add(letters);
        }

        return Collections.emptyList();
    }

    public List<String> findWords(List<String> lettersList) {
        List<List<Character>> results = new ArrayList<>();
        int size = lettersList.size();
        for (int wi = 0; wi < size; ++wi) {
            String letters = lettersList.get(wi);
            //TODO
        }
        return Collections.emptyList();
    }

    private static void recursivePermutation(char[] array, int start, int end, List<String> results) {

        if (start == end) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= end; i++) {
                sb.append(array[i]);
            }
            results.add(sb.toString());
        } else {

            for (int i = start; i <= end; i++) {

                swap(array, i, start);

                recursivePermutation(array, start + 1, end, results);

                swap(array, i, start);
            }
        }
    }

    private static void swap(char[] array, int left, int right) {
        char temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }


    public static void recursivePermutation(String str, List<String> results) {
        char[] array = str.toCharArray();
        recursivePermutation(array, 0, array.length - 1, results);
    }

    public static void main(String[] args) {
        String str = "happy";
        List<String> results = new LinkedList<>();
        recursivePermutation(str, results);
        int i = 0;
        for(String word: results) {
            log.info("{}. {}", ++i, word);
        }
    }

}
