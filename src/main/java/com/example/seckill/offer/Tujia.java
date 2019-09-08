package com.example.seckill.offer;

import java.util.Scanner;

public class Tujia {

    public static void main(String[] args){


        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){

            int M = sc.nextInt();
            int S = sc.nextInt();
            int T = sc.nextInt();

            judge(M,S,T);

        }
    }
    /**
     * 思路：能闪现就闪现
     * */
    private static void judge(int M, int S, int T) {
        //正常13m/s,闪现50m/s,恢复4点/s,闪现一次消耗10
        int time = 0;
        int move = 0;
        int energy = M;
        //计算在雪崩到达前能够走的最大距离
        while(time < T){

            //如果剩余能量能够支持闪现且雪崩还未到达，则闪现
            if(energy - 10 >= 0){
                energy -= 10;
                time ++;
                move += 50;
            }else{
                //魔法值不够闪现后两种情况，一种正常跑，一种等恢复到10点闪现，
                //需要判断哪种方式走的距离远(有可能恢复到10点以上时雪崩到达村庄了)
                //判断如果恢复到10点以上时，雪崩还未到达村庄，那么等待闪现，因为闪现的平均速度肯定比普通走快
                int flashEnergy = energy;
                int flashTime = time;
                boolean canFlash = false;
                //计算等恢复到闪现需要的时间
                while(flashEnergy < 10){
                    flashEnergy += 4;
                    ++flashTime;
                    //一旦魔法值恢复则判断是否能成功闪现
                    //判断能闪现时，雪崩是否到达了,如果未到达，则执行等待闪现
                    if(flashEnergy >= 10 && flashTime < T){
                        canFlash = true;
                    }
                }
                if(canFlash){
                    energy = flashEnergy - 10;
                    time = flashTime + 1;
                    move += 50;
                }else{
                    //在雪崩到达前不能闪现，只能跑步
                    time ++;
                    move += 13;
                }

            }

            //每轮移动后，判断是否到达安全区域
            if(move >= S)
                break;
        }

        if(move >= S){
            System.out.println("Yes");
            System.out.println(time);
        }else{
            System.out.println("No");
            System.out.println(move);
        }
    }
}
