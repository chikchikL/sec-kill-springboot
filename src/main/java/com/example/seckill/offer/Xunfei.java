package com.example.seckill.offer;

import java.util.ArrayList;
import java.util.Scanner;

public class Xunfei {


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while(sc.hasNextLine()){
            String a = sc.nextLine();
            String b = sc.nextLine();

            ArrayList<Integer> sa = new ArrayList<>();
            ArrayList<Integer> sb = new ArrayList<>();

            for(int i = 0;i<a.length();++i){
                sa.add(a.charAt(i)-'0');
            }
            for(int i = 0;i<b.length();++i){
                sb.add(b.charAt(i)-'0');
            }

            String res = new Xunfei().bigAdd(sa, sb);
            System.out.println(res);
        }


    }

    //一个支持两个不等长数值字符串相加的方法
    private String bigAdd(ArrayList<Integer> a, ArrayList<Integer> b){
        while(judgeAllZero(b)){
            //将短的那个数字序列高位补0,使得两个序列长度一致
            if(a.size() > b.size()){
                int cnt = a.size() - b.size();
                while(cnt-- > 0){
                    b.add(0,0);
                }
            }else if(a.size() < b.size()){
                int cnt = b.size() - a.size();
                while(cnt--  > 0){
                    a.add(0,0);
                }
            }
            int i = 0;
            //等长部分和
            while(i<a.size()){
                int temp = a.get(i) + b.get(i);
                a.set(i,temp % 10);
                b.set(i,temp / 10);
                ++i;
            }

            //进位结果左移一位
            b.add(0);

        }

        //移除最终结果高位的0
        while(a.get(0) == 0)
            a.remove(0);

        return String.valueOf(a);


    }

    private boolean judgeAllZero(ArrayList<Integer> arr) {

        boolean hasNonZero = false;
        for (Integer x : arr){
            if(x != 0)
                hasNonZero = true;
        }

        return hasNonZero;
    }


}
