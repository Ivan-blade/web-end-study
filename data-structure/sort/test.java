package sort;

import java.util.Arrays;

public class test {
    
    public static void main(String[] args) {
        int[] arr = new int[] {5,4,7,3,7,2,9,1};
        // bubble(arr);
        // insert(arr);
        // merge(arr, 0, arr.length-1);
        quick(arr, 0, arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    public static void bubble(int[] arr) {
        int len = arr.length;
        for(int i = 0;i < len - 1;i++) {
            for(int j = 0;j < len - i - 1;j++) {
                if(arr[j] > arr[j + 1]) swap(arr, j, j+1);
            }
        }
    } 

    public static void insert(int[] arr) {
        int len = arr.length;
        for(int i = 1;i < len; i++) {
            int temp = arr[i];
            int j = i - 1;
            while(j >= 0 && arr[j] > temp) {
                arr[j+1] = arr[j--];
            }
            arr[++j] = temp;
        }
    }

    public static void merge(int[] arr,int lo,int hi) {
        if(lo >= hi) return;

        int mid = (lo + hi)/2;

        merge(arr, lo, mid);
        merge(arr, mid+1, hi);

        mergesort(arr,lo,mid,hi);
    }

    public static void mergesort(int[] arr,int lo,int mid,int hi) {
        int[] copy = arr.clone();

        int k = lo;
        int i = lo;
        int j = mid+1;

        while(k <= hi) {
            if(i > mid) arr[k++] = copy[j++];
            else if(j > hi) arr[k++] = copy[i++];
            else if(copy[i] < copy[j]) arr[k++] = copy[i++];
            else arr[k++] = copy[j++];
        }
    }

    public static void quick(int[] arr,int left,int right) {
        if(left > right) return;

        int lo = left;
        int cur = left + 1;
        while(cur <= right) {
            if(arr[cur] < arr[lo]) 
            {
                swap(arr, cur, lo+1);
                lo++;
            }
            cur++;
        }
        swap(arr, left, lo);
        quick(arr, left, lo-1);
        quick(arr, lo+1, right);
    }

    public static void swap(int[] arr,int i,int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}