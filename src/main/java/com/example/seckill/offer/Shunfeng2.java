package com.example.seckill.offer;

import java.util.Scanner;

public class Shunfeng2 {


//    24小时计时制是一个广为使用的计时体系。但是不同地方使用的计数进制是不同的，
//    例如，在一个古老的村庄就是使用二进制下的24小时制，这时“11：11”表示的就是3点03分。
//
//    现在给出一个未知的时刻，用形如“a:b”的形式来表示，a，b分别是一个字符串，
//    字符串可以由0-9和A-Z组成，分别代表0-9和10-35。请你求出这个时刻所处的所有可能的进制。

//输入
//    输入仅包含一行，即a:b的形式，a，b的含义及组成如题面所示
//    00002:00130
//输出
//    输出可以包含若干个整数，如果不存在任何一个进制符合要求，则输出“-1”，
//    如果有无穷多的进制数符合条件，则输出“0”，
//    否则按从小到大的顺序输出所有进制数，中间用空格隔开
//    4 5 6
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        while(sc.hasNextLine()){

            String str = sc.nextLine();
            String[] time = str.split(":");
            String a = time[0];
            String b = time[1];

            //给定一个进制，判断是否合法
            //边界，如果两个位置都是0，输出0
            //不存在任意进制满足，返回-1
            //全0满足任意进制
            boolean all_zero = true;
            for(int i = 0;i<a.length();++i){
                if(a.charAt(i) != '0'){
                    all_zero = false;
                    break;
                }
            }
            if(all_zero){
                for (int i = 0;i<b.length();++i){
                    if(b.charAt(i) != '0'){
                        all_zero = false;
                        break;
                    }
                }
            }

            if(all_zero){
                System.out.println(0);
                return;
            }


            //不存在进制满足的情况在于，进制过小，tar-1小于某个位置上的数字
            int i = 2;
            StringBuilder sb = new StringBuilder();
            Shunfeng2 sf = new Shunfeng2();
            while(i<=24){
                if(sf.judge(a,b,i))
                    sb.append(i+" ");
                ++i;
            }

            System.out.println(sb.toString());

        }
    }

    //判断输入是否能满足指定进制
    private boolean judge(String h,String m,int tar){

        //合法条件为:
        //1.每个位置数字应该小于当前tar
        //2.按照进制计算的时<=23，分<=59
        int[] res_h = getTotal(h, tar);
        if(res_h[0] == -1 || res_h[1] >= 24){
            return false;
        }

        //判断分钟合法
        int[] res_m = getTotal(m, tar);
        if( res_m[0] == -1 || res_m[1] >= 60){
            return false;
        }

        return true;
    }

    //获取一个字符串按指定进制转化为10进制的值
    private int[] getTotal(String hm, int tar) {
        char[] chars = hm.toCharArray();
        int total = 0;
        //res[0]为-1的话表示有数字超过当前进制最大数值
        int[] res = new int[2];
        for(int i = chars.length - 1;i>=0;--i){
            char temp = chars[i];
            int cur = 0;
            int cur_pow = chars.length -1 - i;
            //0~9，A~Z
            if(temp >= '0' && temp <= '9')
                cur = temp - '0';
            else if(temp >= 'A' && temp <= 'Z')
                cur = temp - 'A'+ 10;

            //判断当前位置数字是否<tar，非法立即返回
            if(cur >= tar){
                res[0] = -1;
                return res;
            }

            //累加
            total += (int)(Math.pow(tar,cur_pow)) * cur;
        }

        res[1] = total;
        return res;
    }


}
