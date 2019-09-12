package com.example.seckill.offer;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

public class Aiqiyi2 {
//    同样是一个袋子里面有n个红球和m个蓝球，共有A，B，C三人参与游戏
//    三人按照A，B，C的顺序轮流操作，在每一回合中，A，B，C都会随机从袋子里面拿走一个球，
//    然而真正分出胜负的只有A，B两个人，没错，C就是来捣乱的，他除了可以使得袋子里面减少一个球，
//    没有其他任何意义，
//    而A，B谁 先拿到红球就可以获得胜利，但是由于C的存在，两人可能都拿不到红球，此时B获得胜利。


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);


        while(sc.hasNextInt()){
            int n = sc.nextInt();
            int m = sc.nextInt();
            Aiqiyi2 solution = new Aiqiyi2();
            solution.dfs(n,m);
            int cnta = solution.res[0];
            int cntb = solution.res[1];
            double v = (double) cnta / (double) (cntb + cnta);
            BigDecimal bigDecimal = new BigDecimal(v);
            double result = bigDecimal.setScale(BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println(result);
        }
    }

    //所有可能中A，B分别获胜的次数
    int[] res = new int[2];
    //递归找出所有结果,n为剩余红球数，m为剩余蓝球数
    //A要获胜，只有取到红球，其他情况都是B获胜
    private void dfs(int n,int m){
        //1.成功走到这代表A，B都不能取到红，B胜利
        if(n == 0){
            res[1]++;
            return;
        }

        //2.只有红球A首先拿必获胜
        if(n > 0 && m == 0){
            res[0]++;
            return;
        }

        //ABC三人轮流取球
        //走到这必有红蓝球存在

        //A取到红，A获胜
        if(n>0){
            res[0]++;
            --m;
        }

        //A没取到红,且没有蓝球了,B必获胜
        if(n > 0 && m == 0){
            res[1]++;
            return;
        }

        //若A、B都取的篮球,肯定有红球存在，胜负未定，需要递归
        //有蓝球,不确定
        if(m > 0){
            //C取篮
            dfs(n,m-1);
            //C取红
            dfs(n-1,m);
        }else if(n > 0){
            //无蓝且有红球,直接A获胜
            res[0]++;
        }

    }
}
