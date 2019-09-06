package com.example.seckill.offer;

import java.util.Scanner;

public class Main2 {



    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while(sc.hasNextInt()){
            int n = sc.nextInt();
            int res = new Main2().findBitOneCntOfNum(n);

            System.out.println(res);
        }

    }


    private int findBitOneCntOfNum(int n){

        int cnt=0;
        //每轮(n-1) & n可以将n的最右边一个1变为0
        while(n!=0){

            n &= n-1;
            ++cnt;
        }

        return cnt;
    }
}
