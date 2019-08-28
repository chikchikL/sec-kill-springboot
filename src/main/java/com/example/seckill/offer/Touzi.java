package com.example.seckill.offer;

import java.util.Arrays;
import java.util.Scanner;

public class Touzi {

    int[] res;
    //递归计算n个骰子所有点数的概率
    //每层递归需要带着本层的结果走到下层,cur代表当前和
    void recur(int n,int cur){

        //递归截止条件
        if(n == 0){
            res[cur] += 1;
            return;
        }
        for(int i = 1;i<=6;++i){
            recur(n-1,cur + i);
        }
    }

    //举例，共两个骰子
    //第一轮[0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0]
    //第二轮[0, 0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1]
    //数组下标为和，值为该和出现的次数，每增加一个骰子，所有和出现的次数
    //依赖于上一个骰子发生的结果，并且对于新一轮的骰子次数统计，只有n-6~n-1的上一轮结果
    //才会使得n发生次数增加一次，因此n发生次数是上一轮为止n-6~n-1和出现次数的和
    int[] arr1,arr2;
    int round = 1;
    void cyclic(int n) {
        //所有可能基于第一个骰子的投掷结果
        for(int i = 1;i<=6;++i){
            arr1[i] = 1;
        }
        //共n轮循环,标记本轮轮到哪个数组更新
        while (round < n) {
            if (round % 2 == 1) {
                //根据arr2的数据更新arr1
                for (int i = 1; i <=n  * 6; ++i) {
                    int temp = 0;
                    for (int j = i - 1; j > 0 && j >= i - 6; --j) {
                        temp += arr1[j];
                    }
                    arr2[i] = temp;
                }
            } else {
                //根据arr2的数据更新arr1
                for (int i = 1; i <= n * 6; ++i) {
                    int temp = 0;
                    for (int j = i - 1; j > 0 && j >= i - 6; --j) {
                        temp += arr2[j];
                    }
                    arr1[i] = temp;
                }
            }
            ++round;
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){
            int n = sc.nextInt();
            Touzi touzi = new Touzi();

            //递归生成
            touzi.res = new int[6 * n + 1];
            touzi.recur(n,0);
            String string = Arrays.toString(touzi.res);
            System.out.println("递归结果："+string);


            touzi.arr1 = new int[6 * n + 1];
            touzi.arr2 = new int[6 * n + 1];
            touzi.cyclic(n);
            if(touzi.round % 2 == 0){
                System.out.println("非递归结果：" + Arrays.toString(touzi.arr2));
            }else{
                System.out.println("非递归结果："+Arrays.toString(touzi.arr1));
            }

        }
    }
}
