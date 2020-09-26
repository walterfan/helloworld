package com.github.walterfan.helloalgorithm;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @Author: Walter Fan
 **/
@Slf4j
public class SortCardTask implements Callable<Long> {
    public enum SortMethod { BUBBLE_SORT, INSERT_SORT, QUICK_SORT, MERGE_SORT, TIM_SORT}
    private final List<Poker.Card> cards;
    private final SortMethod sortMethod;
    private final int taskNumber;

    private final AtomicInteger taskCounter;

    public SortCardTask(List<Poker.Card> cards, SortMethod method, int taskNumber, AtomicInteger taskCounter) {
        this.cards = cards;
        this.sortMethod = method;
        this.taskNumber = taskNumber;
        this.taskCounter = taskCounter;
    }

    @Override
    public Long call() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("* {} begin to sort {} cards ({} suite） by {}", this.taskNumber, cards.size(), cards.size()/52, sortMethod);
        Comparator<Poker.Card> comparator = new Poker.CardComparator();
        switch(sortMethod) {
            case BUBBLE_SORT:
                bubbleSort(cards, comparator);
                break;
            case INSERT_SORT:
                insertSort(cards, comparator);
                break;
            case QUICK_SORT:
                quickSort(cards, comparator);
                break;
            case MERGE_SORT:
                mergeSort(cards, comparator);
                break;
            case TIM_SORT:
                timSort(cards, comparator);
                break;
        }

        stopwatch.stop();

        long millis = stopwatch.elapsed(MILLISECONDS);
        log.info("* {} end to sort {} cards ({} suite）sort by {} spend {} milliseconds - {}" , this.taskNumber, cards.size(), cards.size()/52, sortMethod, millis, stopwatch); // formatted string like "12.3 ms"
        taskCounter.incrementAndGet();
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
        //aList.parallelStream().sorted(comparator).collect(Collectors.toList());
        aList.sort(comparator);
    }

    public static <T> void quickSort(List<T> aList, Comparator<T> comparator) {
        T[] arr = (T[]) aList.toArray();
        quickSort(arr, comparator, 0, aList.size() - 1);
    }

    public static <T> void quickSort(T[] arr, Comparator<T> comparator, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, comparator, begin, end);

            quickSort(arr, comparator, begin, partitionIndex-1);
            quickSort(arr, comparator, partitionIndex+1, end);
        }
    }

    private static <T> int partition(T[] arr, Comparator<T> comparator, int begin, int end) {

        T pivot = arr[end];
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (comparator.compare(arr[j], pivot) <= 0) { //arr[j] <= pivot
                i++;

                T swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        T swapTemp = arr[i+1];
        arr[i+1] = arr[end];
        arr[end] = swapTemp;

        return i+1;
    }

    public static <T> void mergeSort(List<T> aList, Comparator<T> comparator) {
        T[] arr = (T[]) aList.toArray();
        mergeSort(arr, 0, aList.size() - 1, comparator);
    }

    public static <T> void mergeSort(T arr[], int l, int r, Comparator<T> comparator)
    {
        if (l < r) {
            // Same as (l+r)/2, but avoids overflow for
            // large l and h
            int m = l + (r - l) / 2;

            // Sort first and second halves
            mergeSort(arr, l, m, comparator);
            mergeSort(arr, m + 1, r, comparator);

            merge(arr, l, m, r, comparator);
        }
    }

    public static <T> void  merge(T arr[], int l, int m, int r, Comparator<T> comparator)
    {
        int i, j, k;
        int n1 = m - l + 1;
        int n2 = r - m;

        /* create temp arrays */
        T[] L = (T[])new Object[n1];
        T[] R = (T[])new Object[n2];

        /* Copy data to temp arrays L[] and R[] */
        for (i = 0; i < n1; i++)
            L[i] = arr[l + i];
        for (j = 0; j < n2; j++)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays back into arr[l..r]*/
        i = 0; // Initial index of first subarray
        j = 0; // Initial index of second subarray
        k = l; // Initial index of merged subarray
        while (i < n1 && j < n2) {
            if (comparator.compare(L[i], R[j]) <= 0) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

    /* Copy the remaining elements of L[], if there
       are any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

    /* Copy the remaining elements of R[], if there
       are any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }



    @Override
    public String toString() {
        return "SortCardTask{" +
                "taskNumber=" + taskNumber +
                ", sortMethod=" + sortMethod +
                '}';
    }
}
