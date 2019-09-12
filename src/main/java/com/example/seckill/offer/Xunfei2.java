package com.example.seckill.offer;

import java.util.Scanner;

public class Xunfei2 {


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){

            String str = sc.nextLine();

            String compress = new Xunfei2().compress(new StringBuffer(str));
            System.out.println(compress);
        }
    }


    private String compress(StringBuffer sb){

        if(sb.length() == 0)
            return sb.toString();

        //滑动窗口
        //从前往后遍历end,达到字符串末尾
        //end与start不同时、end到大字符串末尾时东欧需要判定当前窗口内字符是否可以压缩
        int start = 0,end = 0;
        for(;end< sb.length();++end){
            if(sb.charAt(end) == sb.charAt(start))
                continue;
            else{
                //判断是否有重复值，替换start~end-1位置的字符串为
                if(end - start > 1){
                    sb.replace(start,end,end-start+""+sb.charAt(start));
                }
                start = end;
            }
        }

        if(end - start > 1){
            sb.replace(start,end,end-start+""+sb.charAt(start));
        }


        return sb.toString();
    }


}
