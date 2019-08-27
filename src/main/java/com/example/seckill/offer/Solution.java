package com.example.seckill.offer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Solution {

    //荷兰国旗，将arr按照tar分割
    public int[] patition(int [] arr,int l,int r,int tar){

        //想象两个区域less和more
        int less = l - 1,more = r+1;
        //从前往后遍历
        int i = l;
        while(i < more){
            if(arr[i] < tar){
                swap(arr,i++,++less);
            }else if(arr[i] > tar){
                //交换过来的数字不一定大还是小，进入下一次判断
                swap(arr,i,--more);
            }else{
                //碰到相等的直接往后遍历
                ++i;
            }
        }

        return new int[]{less + 1,more - 1};
    }

    public void quickSort(int[] arr,int l,int r){
        if(r > l){
            //随机快排
            swap(arr,l+(int)(Math.random()*(r-l+1)),r);
            int[] borders = patition(arr, l, r, arr[r]);
            //递归处理
            quickSort(arr,l,borders[0]-1);
            quickSort(arr,borders[1]+1,r);


        }


    }

    public void swap(int[] arr,int l,int r){
        int temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;



    }

    public static void main(String[] args){
//        int[] arr = {3,123,4332,343,32,1,3,2,34,1,-1,0};
//        new Solution().quickSort(arr,0,arr.length-1);
//        System.out.println(Arrays.toString(arr));


        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){
            int idx = sc.nextInt();
            //标记几位数
            int wei = 1;
            while(true){
                int sum = getSumOf(wei);
                if(sum >= idx){
                    //在这个范围内，从这个范围内找
                    System.out.println("result = "+findRes(idx,wei));
                    return;
                }
                idx -= sum;
                ++wei;
            }
        }

    }

    //在n位数范围内找第idx个元素
    private  static int  findRes(int idx, int n) {
        //初始idx=111的话，111-10 = 101 ，180>=100，在2位数里找第101个元素=50*2+1
        int cnt = idx / n;//第51个元素的第1个数字
        int yu = idx % n;

        return 0;

    }

    //获取n位数的总数
    static int getSumOf(int n){
        //计算n位数的个数
        double v = Math.pow(10, n) - Math.pow(10, n - 1);
        int cnt = (int)v;
        if(n == 1)
            cnt++;

        return cnt;
    }
}
