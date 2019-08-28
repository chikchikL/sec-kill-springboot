package com.example.seckill.offer;

import java.util.Scanner;

public class Dp1 {


    //求一个数组的最长递增序列，不要求元素连续
    //dp[i],i位置的最长递增序列，与之前所有的dp[0~i-1]都有关，
    //并且只有在arr[i] >= arr[0~i-1]时才能加入该序列
    //状态转移方程 dp[i] = max{1,dp[0~i-1] + 1,条件为arr[i] >= arr[j] ,j = 0~i-1}
    public int dp1(int[] arr){
        int[] dp = new int[arr.length];
        dp[0] = 1;
        for(int i =1;i<arr.length;++i){
            //至少为当前数字单独作为序列
            int max = 1;
            for(int j = 0;j<i;++j){
                //这里的dp[j] > dp[i]纯粹为了在遍历过程中获取最大值
                if(arr[i] >= arr[j]){
                    if(dp[j] + 1 > max){
                        max = dp[j]+1;
                    }
                }
            }
            dp[i] = max;
        }
        int maxLen = 0;
        //遍历dp找到最值
        for(int i = 0;i<arr.length;++i){
            if(dp[i] > maxLen)
                maxLen = dp[i];
        }
        return maxLen;
    }




    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){
            int len = sc.nextInt();
            int arr[] = new int[len];
            for(int i = 0;i< len; ++i){
                arr[i] = sc.nextInt();
            }

            System.out.println("最长非连续递增序列长度为:"+new Dp1().dp1(arr));
        }
    }

}
