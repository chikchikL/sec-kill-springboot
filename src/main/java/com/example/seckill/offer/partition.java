package com.example.seckill.offer;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

@Component("")
public class partition {


    //荷蘭國旗
    public int[] helanguoqi(int [] arr, int l, int r, int tar){

        //非法
        if(arr == null || arr.length < 2)
            return arr;

        //在数组两边想象两个区域，从前往后遍历，遇到比i位置小的换前面去
        //遇到比i大的换后面去
        //循环截止条件，i与more指针没有相遇

        int less = l-1;
        int more = r+1;
        int i = l;

        while(i < more){

            if(arr[i] < tar){
                swap(arr,i++,++less);
            }else if(arr[i] > tar){
                swap(arr,i,--more);
            }else{
                ++i;
            }

        }

        return new int[]{less + 1,more-1};


    }

    void swap(int[] arr,int l,int r){
        int temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;
    }

    public void quicksort(int[] arr,int l,int r){
        if(r>l){
            swap(arr,l+(int)(Math.random() * (r-l+1)),r);
            int[] boards = helanguoqi(arr, l, r, arr[r]);
            quicksort(arr,l,boards[0]-1);
            quicksort(arr,boards[1]+1,r);

        }
    }

    public static void main(String[] args){
        int[] arr = new int[]{1,5,1,4,0,9,1,3,2,4};
//        new partition().helanguoqi(arr,0,arr.length-1,4);
//        System.out.println(Arrays.toString(arr));
        new partition().quicksort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }
}
