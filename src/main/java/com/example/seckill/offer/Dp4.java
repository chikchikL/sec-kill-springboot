package com.example.seckill.offer;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//硬币问题
public class Dp4 {

    public static void main(String[] args){
        int[] coins = {1,2,5,11};
        int i = new Solution().coinChange(coins, 11);
        System.out.println(i);
    }
    static class Solution {
        public int coinChange(int[] coins, int amount) {
            //dp三要素
            //最优子结构 f(10) = min{f(10-5),f(10-2),f(10-1)}

            //边界
            //遍历coins,f(coins[i]) = 1;
            //bfs,递归删除

            //状态转移方程 f(t)凑成金额t的最小硬币个数，若凑不出为-1
            //f(t) = min{f(amount - coins[0~len-1])}
            rem.clear();
            return findMin(coins,amount);
        }

        //先写出递归解法后进行优化,cnt记录硬币数量
        HashMap<Integer,Integer> rem = new HashMap<>();
        private int findMin(int[] coins,int total){
            //成功组合be
            if(total == 0){
                return 0;
            }

            if(total < 0){
                return -1;
            }

            //备忘过的组合
            if(rem.get(total) != null)
                return rem.get(total);

            //记录最小硬币数
            int min = Integer.MAX_VALUE;
            for(int coin:coins){
                int temp = findMin(coins,total - coin);
                //-1的话直接考虑下种可能
                if(temp == -1)
                    continue;
                //返回的非0解+1取到最小值
                if(min > temp + 1)
                    min = temp + 1;
            }

            ExecutorService executorService = Executors.newCachedThreadPool();
            rem.put(total,(min == Integer.MAX_VALUE) ? -1 : min);
            //如果没有成功的组合，直接返回-1
            return (min == Integer.MAX_VALUE) ? -1 : min;
        }

        //动态规划解法
        private int dpFind(int[] coins,int total){
            //状态转移方程
            //f(x) 代表凑总价值x的最小硬币数量
            //f(0) = 0
            int[] dp = new int[total + 1];
            //边界
            dp[0] = 0;

            for(int i = 1;i<=total;++i){
                //f(x) = min{f(x-cj),j=coins.length} + 1
                int min = Integer.MAX_VALUE;
                //尝试找到子问题的解，没有则返回-1
                for(int coin:coins){

                    if(i - coin < 0)
                        continue;
                    if(dp[i-coin] == -1)
                        continue;
                    if(min > dp[i-coin])
                        min = dp[i-coin];

                }
                dp[i] = (min == Integer.MAX_VALUE ? -1 : min + 1);
            }

            return dp[total];

        }
    }
}
