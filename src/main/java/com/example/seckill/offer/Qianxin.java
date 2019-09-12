package com.example.seckill.offer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Qianxin {


// 给定n个进程，这些进程满足以下条件：
// (1）每个进程有唯一的PID，其中PID为进程ID
//（2）每个进程最多只有一个父进程，但可能有多个子进程，用PPID表示父进程ID
//（3）若一个进程没有父进程，则其PPID为0
//（4）PID、PPID都是无符号整数
// 结束进程树的含义是当结束一个进程时，它的所有子进程也会被结束，包括子进程的子进程。
// 现在给定大小为n的两组输入列表A和B（1 <= n <= 100），列表A表示进程的PID列表，
// 列表B表示列表A对应的父进程的列表，即PPID列表。
// 若再给定一个PID，请输出结束该PID的进程树时总共结束的进程数量。
//输入：
//3 1 5 21 10
//0 3 3 1 5
//5
//输出：
//2
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){
            String str = sc.nextLine();
            String[] pidStrs = str.split(" ");
            ArrayList<Integer> pids = new ArrayList<>();
            for(String s:pidStrs){
                pids.add(Integer.valueOf(s));
            }
            ArrayList<Integer> ppids = new ArrayList<>();
            String[] ppidStrs = sc.nextLine().split(" ");
            for(String s:ppidStrs){
                ppids.add(Integer.valueOf(s));
            }
            String tarStr = sc.nextLine();
            Integer tar = Integer.valueOf(tarStr);
            int res = new Qianxin().killProcess(pids, ppids, tar);
            System.out.println(res);
        }


    }

    private int killProcess(ArrayList<Integer> pids,ArrayList<Integer> ppids,int tar){

        //指定的进程号有可能不存在
        boolean exist = false;
        for(Integer pid:pids){
            if(tar == pid)
                exist = true;
        }
        if(!exist)
            return 0;
        //bfs
        //队列初始为tar,出队元素x，res++
        //找ppid表中为x的元素,将其子进程入队
        //循环出队直到空
        LinkedList<Integer> queue = new LinkedList<>();
        int cnt = 0;
        queue.offer(tar);
        int size = ppids.size();
        while(!queue.isEmpty()){
            Integer x = queue.pollLast();
            ++cnt;
            //遍历父进程表中ppid为x的元素
            for(int i = 0;i<size;++i){
                if(ppids.get(i).equals(x))
                    queue.offer(pids.get(i));
            }
        }

        return cnt;
    }

}
