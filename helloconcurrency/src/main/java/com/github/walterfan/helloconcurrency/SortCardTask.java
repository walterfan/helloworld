package com.github.walterfan.helloconcurrency;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @Author: Walter Fan
 * @Date: 27/3/2020, Fri
 **/
@Slf4j
public class SortCardTask implements Callable<Long> {
    public enum SortMethod { BUBBLE_SORT, INSERT_SORT, TIM_SORT}
    private final List<Poker.Card> cards;
    private SortMethod sortMethod;

    public SortCardTask(List<Poker.Card> cards, SortMethod method) {
        this.cards = cards;
        this.sortMethod = method;
    }

    @Override
    public Long call() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        switch(sortMethod) {
            case BUBBLE_SORT:
                bubbleSort(cards, new Poker.CardComparator());
                break;
            case INSERT_SORT:
                insertSort(cards, new Poker.CardComparator());
                break;
            case TIM_SORT:
                timSort(cards, new Poker.CardComparator());
                break;
        }

        stopwatch.stop();
        long millis = stopwatch.elapsed(MILLISECONDS);
        log.info("{} cards sort by {} spend {} milliseconds - {}" , cards.size(), sortMethod, millis, stopwatch); // formatted string like "12.3 ms"
        return millis;
    }

    public static <T> void bubbleSort(List<T> aList, Comparator<T> comparator) {
        boolean sorted = false;
        int loopCount = aList.size() - 1;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < loopCount; i++) {
                if (comparator.compare(aList.get(i), aList.get(i + 1)) > 0) {
                    Collections.swap(aList, i, i + 1);
                    sorted = false;
                }
            }
        }
    }

    public static <T> void insertSort(List<T> aList, Comparator<T> comparator) {
        int size = aList.size();
        for (int i = 1; i < size; ++i) {
            T selected = aList.get(i);

            if (size < 10) {
                log.info("{} insert to {}", selected, aList.subList(0, i).stream().map(String::valueOf).collect(Collectors.joining(", ")));
            }

            int j = i - 1;
            //find a position for insert currentElement in the left sorted collection
            while (j >= 0 && comparator.compare(selected, aList.get(j)) < 0) {
                //it does not overwrite existed element because the j+1=i that is currentElement at beginging
                aList.set(j + 1, aList.get(j));
                j--;
            }
            aList.set(j + 1, selected);

        }
    }

    public static <T> void timSort(List<T> aList, Comparator<T> comparator) {
        aList.stream().sorted(comparator).collect(Collectors.toList());
    }
}
