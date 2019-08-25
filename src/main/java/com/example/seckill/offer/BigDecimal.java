package com.example.seckill.offer;

import java.util.ArrayList;
import java.util.Arrays;

public class BigDecimal {

    //输入n代表位数
    char[] temp ;

    ArrayList<String>  res = new ArrayList<>();
    public ArrayList<String> print(int n){
        temp = new char[n];

        dfs(n,0);

        return res;
    }

    //深度优先遍历
    private void dfs(int n, int idx) {

        //截止条件
        if(idx == n){
            //需要将第一个非零数字前面的数字舍去
            int first = getFirstNonZero(temp);
            if(first == -1)
                return;

            res.add(new String(Arrays.copyOfRange(temp,first,temp.length)));
            return;
        }

        //确认当前数字
        for(int i=0;i<=9;++i){

            temp[idx] = (char)(i+'0');
            //递归确认之后数字
            dfs(n,idx+1);
        }

    }

    public int getFirstNonZero(char[] str){
        for(int i =0;i<str.length;++i){
            if(str[i] != '0')
                return i;
        }

        return -1;
    }

    public static void main(String[] args){
        ArrayList<String> print = new BigDecimal().print(7);
        for (String x :
                print) {
            System.out.println(x);
        }
    }
}
