package com.example.seckill.offer;

import java.util.Scanner;

public class Translation {


    //递归求某个数字字符串翻译成字母字符串的个数
    //f(i) = f(i+1) + g(i,i+1)*f(i+2)
    //f(i)表示从i位置开始的翻译可能数量，g(i,i+1)表示与下个数字拼接是否满足是10~25内的整数
    //普通递归
    int cnt=0;
    public void recur(char[] str,int idx){
        //递归截止条件,以某种方式走到str末尾则算作一种翻译可能
        if(idx == str.length){
            ++cnt;
            return;
        }
        recur(str,idx+1);
        if(idx < str.length - 1){
            //判断与下个数字的连接
            String temp = str[idx]+""+ str[idx + 1];
            Integer value = Integer.valueOf(temp);
            if(value <= 25 && value >= 10){
                recur(str,idx+2);
            }

        }
    }



    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){
            String str = sc.nextLine();
            Translation translation = new Translation();
            translation.recur(str.toCharArray(), 0);
            System.out.println("总共可能的翻译数量为:"+translation.cnt);
        }


    }
}
