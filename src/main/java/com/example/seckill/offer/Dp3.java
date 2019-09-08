package com.example.seckill.offer;

import java.util.Arrays;

//挖金矿问题
//二维空间缩减到一维
//只需要上一行就可以推得下一行，因此不需要保存整个表格的信息
//只需要两个数组，一个缓存上一行状态，一个缓存下一行状态即可
public class Dp3 {
    int N = 5;
    int W = 10;
    int G[] = {0,400,500,200,300,350};
    int P[] = {0,5,5,3,4,3};

    public int getMostVal(){
        //边界
        int [] results = new int[W+1];
        int [] preResults = new int[W+1];

        //向后遍历，初始化索引为0的金矿行
        for(int i = 1;i <= W; ++i){
            if(P[1] > i)
                preResults[i] = 0;
            else
                preResults[i] = G[1];
        }
        System.out.println(Arrays.toString(preResults));
        //遍历剩余行
        for(int i = 2;i <= N;++i){
            for(int j = 1;j <= W;++j){
                //如果不够挖编号为i的金矿
                if(P[i] > j){
                    //实际是dp[i-1][j]
                    results[j] = preResults[j];
                }else{
                    //不挖 dp[i-1][j]
                    //挖编号i   dp[i-1][j-P[i]]+G[i]
                    results[j] = Math.max(preResults[j],preResults[j-P[i]] + G[i]);
                }
            }
            //更新完一行之后缓存,不要直接将results引用传递给preResults，这样两个引用就指向同一个数组了
            preResults = Arrays.copyOf(results,results.length);
            System.out.println(Arrays.toString(preResults));
        }

        return preResults[W];
    }


    public static void main(String[] args){
        int mostVal = new Dp3().getMostVal();
        System.out.println("最大金矿值:"+mostVal);
    }
}
