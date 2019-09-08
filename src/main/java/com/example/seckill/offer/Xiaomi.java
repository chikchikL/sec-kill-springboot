package com.example.seckill.offer;


import java.util.Scanner;

public class Xiaomi {

    static class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val){
            this.val = val;
        }
    }
    /*请完成下面这个函数，实现题目要求的功能
    当然，你也可以不按照下面这个模板来作答，完全按照自己的想法来 ^-^
    ******************************开始写代码******************************/
    static String solution(String input) {
        //递归创建左右子树,每轮创建根节点，并确认左右子树的序列
        TreeNode root = reCreate(input);
        res = new StringBuilder();
        inOrder(root);
        return res.toString();

    }
    static StringBuilder res;
    static void inOrder(TreeNode root){
        if(root != null){
            inOrder(root.left);
            res.append(root.val);
            inOrder(root.right);
        }
    }

    static TreeNode reCreate(String str){

        //递归截止条件,走到空指针
        if(str == null || str.equals("")){
            return null;
        }
        //根节点
        TreeNode root = new TreeNode(str.charAt(0) - '0');

        //没有子树了
        if(str.length() == 1){
            return root;
        }

        //如果根最左边是括号，递归，否则直接返回结点
        if(str.charAt(1) == '('){
            int from = str.indexOf("(");
            int to = str.lastIndexOf(")");
            //去除左右括号
            String left_right = str.substring(from + 1, to);
            //找到左右子树的分割点，即第一个所有括号外的逗号
            //cnt记数，遍历字符串，碰到（+1，碰到）-1，找到第一个cnt为0时的逗号
            int i = 0,cnt = 0;
            while(i < left_right.length()){
                //左右子树分割点
                if(cnt == 0 && left_right.charAt(i) == ','){
                    break;
                }

                if(left_right.charAt(i) == '('){
                    ++cnt;
                }else if(left_right.charAt(i) == ')'){
                    --cnt;
                }
                ++i;
            }
            //以i作为分割点确定左右子树
            String lStr = left_right.substring(0, i);
            String rStr = left_right.substring(i + 1);
            //递归创建左右子树
            root.left = reCreate(lStr);
            root.right = reCreate(rStr);
        }

        return root;
    }
    /******************************结束写代码******************************/


    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String res;

        String _input;
        try {
            _input = in.nextLine();
        } catch (Exception e) {
            _input = null;
        }

        res = solution(_input);
        System.out.println(res);
    }
}


