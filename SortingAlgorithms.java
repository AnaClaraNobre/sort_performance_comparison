import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class SortingAlgorithms {

    // Bubble Sort Sequencial
    public void bubbleSortSequential(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    // Bubble Sort Paralelo
    public void bubbleSortParallel(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ParallelBubbleSortTask(array, 0, array.length - 1));
    }

    // Quick Sort Sequencial
    public void quickSortSequential(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSortSequential(array, low, pivotIndex - 1);
            quickSortSequential(array, pivotIndex + 1, high);
        }
    }

    // Quick Sort Paralelo
    public void quickSortParallel(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ParallelQuickSortTask(array, 0, array.length - 1));
    }

    // Merge Sort Sequencial
    public void mergeSortSequential(int[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSortSequential(array, left, middle);
            mergeSortSequential(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    // Merge Sort Paralelo
    public void mergeSortParallel(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ParallelMergeSortTask(array, 0, array.length - 1));
    }

    // Selection Sort Sequencial
    public void selectionSortSequential(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
    }

    // Selection Sort Paralelo
    public void selectionSortParallel(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ParallelSelectionSortTask(array, 0, array.length - 1));
    }

    //partição para o Quick Sort
    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    // mesclagem para o Merge Sort
    private void merge(int[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    private static class ParallelBubbleSortTask extends RecursiveAction {
        private int[] array;
        private int low, high;
        private static final int THRESHOLD = 500;

        ParallelBubbleSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low < THRESHOLD) {
                new SortingAlgorithms().bubbleSortSequential(array);
            } else {
                int mid = (low + high) / 2;
                ParallelBubbleSortTask leftTask = new ParallelBubbleSortTask(array, low, mid);
                ParallelBubbleSortTask rightTask = new ParallelBubbleSortTask(array, mid + 1, high);
                invokeAll(leftTask, rightTask);
            }
        }
    }

    private static class ParallelQuickSortTask extends RecursiveAction {
        private int[] array;
        private int low, high;
        private static final int THRESHOLD = 500;

        ParallelQuickSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low < THRESHOLD) {
                new SortingAlgorithms().quickSortSequential(array, low, high);
            } else {
                int pivotIndex = new SortingAlgorithms().partition(array, low, high);
                ParallelQuickSortTask leftTask = new ParallelQuickSortTask(array, low, pivotIndex - 1);
                ParallelQuickSortTask rightTask = new ParallelQuickSortTask(array, pivotIndex + 1, high);
                invokeAll(leftTask, rightTask);
            }
        }
    }

    private static class ParallelMergeSortTask extends RecursiveAction {
        private int[] array;
        private int low, high;
        private static final int THRESHOLD = 500;

        ParallelMergeSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low < THRESHOLD) {
                new SortingAlgorithms().mergeSortSequential(array, low, high);
            } else {
                int middle = (low + high) / 2;
                ParallelMergeSortTask leftTask = new ParallelMergeSortTask(array, low, middle);
                ParallelMergeSortTask rightTask = new ParallelMergeSortTask(array, middle + 1, high);
                invokeAll(leftTask, rightTask);
                new SortingAlgorithms().merge(array, low, middle, high);
            }
        }
    }

    private static class ParallelSelectionSortTask extends RecursiveAction {
        private int[] array;
        private int low, high;
        private static final int THRESHOLD = 500;

        ParallelSelectionSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low < THRESHOLD) {
                new SortingAlgorithms().selectionSortSequential(array);
            } else {
                int mid = (low + high) / 2;
                ParallelSelectionSortTask leftTask = new ParallelSelectionSortTask(array, low, mid);
                ParallelSelectionSortTask rightTask = new ParallelSelectionSortTask(array, mid + 1, high);
                invokeAll(leftTask, rightTask);
            }
        }
    }
}
