package com.example.seckill.offer;

import java.util.Arrays;
import java.util.HashMap;

//二维动态规划
public class Dp2 {
    int [][] dp;
    int num,capacity;
    int w[] = { 0 , 2 , 3 , 4 , 5 };
    int v[] = { 0 , 3 , 4 , 5 , 6 };
    public int dp2(){
        new HashMap<>().clear();

        num = 4;
        capacity = 8;
        dp = new int[num+1][capacity+1];
        for(int i = 1;i<=num;++i){
            for(int j = 1;j<=capacity;++j){
                //剩余容量不能放入
                if(j < w[i]){
                    dp[i][j] = dp[i-1][j];
                }else{
                    //足够放入，但是放入当前商品不一定是最优解
                    int max = Math.max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
                    dp[i][j] = max;
                }
            }
        }
        return dp[num][capacity];

    }

    //回溯找到最优解,从num,capacity位置开始找
    int[] solution ;
    public void findSolution(int i,int j){

        //递归的截止条件
        if(i<=0)
            return;

        //确定没放
        if(dp[i-1][j] == dp[i][j]){
            solution[i] = 0;
            findSolution(i-1,j);
        }
        //确定放了
        else if(dp[i-1][j-w[i]] + v[i] == dp[i][j]){
            solution[i] = 1;
            findSolution(i-1,j-w[i]);
        }
    }

    public static void main(String[] args){
//        Dp2 dp2 = new Dp2();
//        int i = dp2.dp2();
//        System.out.println("最大价值"+i);
//
//        //回溯找到具体解
//        dp2.solution = new int[dp2.num+1];
//
//        dp2.findSolution(dp2.num,dp2.capacity);
//        System.out.println("结果:"+ Arrays.toString(dp2.solution));

        int [] arr = new int[100];
        for(int i = 0;i< arr.length;++i)
            arr[i] = i;

        int[] newints = Arrays.copyOf(arr, 50);
        System.out.println(Arrays.toString(newints));
    }
}
