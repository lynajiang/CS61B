import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    /**
     * create a green array,
     * place i
     */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k == 0) {
                return;
            }
            for (int i = 0; i < k; i++) {
                int m = i;
                for (int j = i - 1; j >= 0; j--) {
                    if (array[m] < array[j]) {
                        int temp = array[j];
                        array[j] = array[m];
                        array[m] = temp;
                        m -= 1;
                    }
                }

            }


        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k == 0) {
                return;
            }
            for (int i = 0; i < k; i++) {
                int min = array[i];
                int index = i;
                for (int j = i + 1; j < k; j++) {
                    if (min > array[j]) {
                        min = array[j];
                        index = j;
                    }
                }
                int temp = array[i];
                array[i] = array[index];
                array[index] = temp;

            }

        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /**
     * Your mergesort implementation. An iterative merge
     * method is easier to write than a recursive merge method.
     * Note: I'm only talking about the merge operation here,
     * not the entire algorithm, which is easier to do recursively.
     */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            System.arraycopy(mergeSort(array, k), 0, array, 0, k);

        }

        private int[] mergeSort(int[] array, int k) {
            if (k == 1) {
                return array;
            }

            int[] leftArray;
            int[] rightArray;
            if (k % 2 != 0) {
                leftArray = new int[k / 2 + 1];
                rightArray = new int[k / 2];
            } else {
                leftArray = new int[k / 2];
                rightArray = new int[k / 2];
            }
            System.arraycopy(array, 0, leftArray, 0, leftArray.length);
            System.arraycopy(array, leftArray.length, rightArray, 0, rightArray.length);

            leftArray = mergeSort(leftArray, leftArray.length);
            rightArray = mergeSort(rightArray, rightArray.length);
            return merge(leftArray, rightArray);
        }

        private int[] merge(int[] array1, int[] array2) {
            int[] mergedArray = new int[array1.length + array2.length];
            int one = 0, two = 0, merged = 0;
            while (one < array1.length && two < array2.length) {
                if (array1[one] > array2[two]) {
                    mergedArray[merged++] = array2[two++];
                } else {
                    mergedArray[merged++] = array1[one++];
                }
            }

            while (one < array1.length) {
                mergedArray[merged++] = array1[one++];
            }
            while (two < array2.length) {
                mergedArray[merged++] = array2[two++];

            }
            return mergedArray;
        }


        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /**
     * Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /**
     * Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            System.arraycopy(helper(a, k), 0, a, 0, k);


        }

        private int[] helper(int[] a, int k) {
            int[] newArray = new int[k];
            System.arraycopy(a, 0, newArray, 0, k);
            Queue<Integer>[] buckets = new Queue[10];
            for (int i = 0; i < 10; i++) {
                buckets[i] = new LinkedList<>();
            }
            boolean sorted = false;
            int digit = 1;
            while (!sorted) {
                sorted = true;
                for (int item : newArray) {
                    int bucket = (item / digit) % 10;
                    if (bucket > 0) {
                        sorted = false;
                    }
                    buckets[bucket].add(item);
                }

                digit *= 10;
                int index = 0;
                for (Queue<Integer> bucket : buckets) {
                    while (!bucket.isEmpty()) {
                        newArray[index++] = bucket.remove();
                    }
                }

            }
            return newArray;
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }


        /**
         * MSD Sort implementation.
         */
        public static class MSDSort implements SortingAlgorithm {
            @Override
            public void sort(int[] a, int k) {
                // FIXME
            }

            @Override
            public String toString() {
                return "MSD Sort";
            }
        }

        /**
         * Exchange A[I] and A[J].
         */
        private static void swap(int[] a, int i, int j) {
            int swap = a[i];
            a[i] = a[j];
            a[j] = swap;
        }
    }
}


