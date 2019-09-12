package com.example.seckill.offer;

import java.util.Scanner;

public class Aiqiyi {

//    给定一个长度为N-1且只包含0和1的序列A1到AN-1，
//    如果一个1到N的排列P1到PN满足对于1≤i<N，当Ai=0时Pi<Pi+1，当Ai=1时Pi>Pi+1，
//    则称该排列符合要求，那么有多少个符合要求的排列？

    private int getCnt(){
        return 0;
    }
    
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){

            int N = sc.nextInt();
            int[] A = new int[N - 1];
            for(int i = 0;i<N-1;++i){
                A[i] = sc.nextInt();
            }

            System.out.println();
        }
    }
}
