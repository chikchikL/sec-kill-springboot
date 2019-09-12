package com.example.seckill.offer;

import java.util.Scanner;

public class Shunfeng {

//    我们现在需要在一个二维网格上画一个封闭图形，你有两种操作：
//
//            1. 连接一个1*1格子的对角线。
//
//            2. 连接一个1*1格子的一条边。
//
//    已知你每分钟只能选择一个操作，现在要求你画出一个面积至少为m的多边形，请问你至少需要多长时间？
    
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        Shunfeng sf = new Shunfeng();
        while(sc.hasNextInt()){
            int cnt = sc.nextInt();
            for(int i = 0;i < cnt; ++i){
                int cur = sc.nextInt();
                System.out.println(sf.getMinTime(cur));
            }


        }
    }

    //在二维网格上画指定面积的最小时间
    public int getMinTime(int tar){

        return 0;
    }
}
