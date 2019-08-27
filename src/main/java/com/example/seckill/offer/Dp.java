package com.example.seckill.offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Dp {

    //礼物最大价值

    public static void main(String[] args){


        Scanner sc = new Scanner(System.in);
        //输入m,n
        while(sc.hasNextInt()){
            int m = sc.nextInt();
            int n = sc.nextInt();

            int[][] arr = new int[m][n];

            //输入二维数组
            for(int i = 0;i<m;++i){
                for(int j = 0;j<n;++j){
                    arr[i][j] = sc.nextInt();
                }
            }

            findMaxVal(arr,m,n);
        }
    }




    private static void findMaxVal(int[][] arr,int m,int n) {

        //来到一个位置的最大价值，取决于其之前是从上方还是左边过来的arr[i] + max{上方元素,左边元素}
        int[][] dp = new int[m][n];
        for(int x = 0; x < m;++x){
            for(int y = 0;y < n;++y){
                //没有左边或者上边元素记录为0
                int left = 0,up = 0;
                if(x>0)
                    up = dp[x - 1][y];
                if(y>0)
                    left = dp[x][y - 1];

                int max = Math.max(left, up);

                dp[x][y] = arr[x][y] + max;
            }
        }
        System.out.println(dp[m-1][n-1]);

    }
}
