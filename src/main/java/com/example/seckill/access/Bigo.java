package com.example.seckill.access;

import java.util.Scanner;

public class Bigo {

//    给定一个非空字符串s 和一个包含非空单词列表的字典 wordDict，
//    判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词。（要求用Java实现）
//    示例1：
//    输入: s="leetcode",wordDict = ["leet", "code"]
//    输出: true 解释: 返回true，因为"leetcode"可以被拆分成"leet code"。
//    示例2：
//    输入: s="applepenapple",wordDict = ["apple", "pen"]
//    输出: true 解释: 返回true，因为 "applepenapple" 可以被拆分成 "apple pen apple"。
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while(sc.hasNextLine()){

            String str = sc.nextLine();
            String[] dict = sc.nextLine().split(",");
            System.out.println(new Bigo().judge(str,dict));
        }
    }

    private boolean judge(String str,String[] dict){

        String res = str;
        for(String word:dict){
            if(res.contains(word)){
                res = res.replace(word,"");
            }
        }
        return "".equals(res);
    }
}
