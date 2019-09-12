package com.example.seckill.offer;

import java.util.Scanner;

public class Qianxin2 {

//二元查找树（
// 1.若左子树不空，左子树值都小于父节点；
// 2.如右子树不空，右子树值都大于父节点；
// 3.左、右子树都是二元查找树；
// 4. 没有键值相等的节点）上任意两个节点的值，请找出它们最近的公共祖先。
//输入三行行，第一行为树层级，第二行为数节点（其中-1表示为空节点），第三行为需要查找祖先的两个数。
//在例图中（虚线框没有真实节点，为了输入方便对应位置输-1）查找12和20的最近公共祖先输入为：
//4
//9 6 15 2 -1 12 25 -1 -1 -1 -1 -1 -1 20 37
//12 20

//输出15
//    public static void main(String[] args){
//
//        Scanner sc = new Scanner(System.in);
//
//        while(sc.hasNextInt()){
//            int levels = sc.nextInt();
//            String[] nodesStr = sc.nextLine().split(" ");
//            int x = sc.nextInt();
//            int y = sc.nextInt();
//
//            new Qianxin2().find(levels,nodesStr,String.valueOf(x),String.valueOf(y));
//        }
//    }


//
//    private int find(int levels, String[] nodes, String x, String y){
//
//        //总结点数
//        int cnt = (int) Math.pow(2, levels - 1);
//        //获取x，y层号
//        int idx_x,idx_y;
//        for(int i= 0;i<cnt;++i){
//            if(nodes[i].equals(x)){
//                idx_x = i;
//            }
//            if(nodes[i].equals(y)){
//
//            }
//        }
//
//
//
//    }
//
//    private int getLevel(){
//
//    }
//
//
//
//    private int findFatherIdx(String[] nodes,String tar){
//
//    }




}
