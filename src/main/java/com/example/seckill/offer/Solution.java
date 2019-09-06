package com.example.seckill.offer;

import java.util.Scanner;

public class Solution {
    public String getMinVersion(String[] list) {
        if(list == null || list.length == 0)
            return "";

        return mergeSort(list,0,list.length-1);
    }

    private String mergeSort(String[] list,int l,int r) {

        if(r-l>1){
            int mid = (l+r)/2;
            String lStr = mergeSort(list,l,mid);
            String rStr = mergeSort(list,mid,r);
            return findMinor(lStr,rStr);
        }else{
            return findMinor(list[l],list[r]);
        }
    }

    //从两个字符串中找到较小版本号
    public String findMinor(String s1,String s2){

        String[] vers1 = s1.split("\\.");
        String[] vers2 = s2.split("\\.");

        int i1=0,i2 = 0;
        while(i1 < vers1.length || i2 < vers2.length){
            //找到第一个不相同的子版本号位置
            //谁先结束谁小?
            if(i1 == vers1.length){
                return s1;
            }else if(i2 == vers2.length){
                return s2;
            }else if(vers1[i1].equals(vers2[i2])){
                i1++;
                i2++;
            }else{
                //转为数字比较大小
                Integer ver1 = Integer.valueOf(vers1[i1].trim());
                Integer ver2 = Integer.valueOf(vers2[i2].trim());
                if(ver1 < ver2)
                    return s1;
                else
                    return s2;
            }
        }

        //走到这代表
        return s1;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while(sc.hasNextLine()){
            String temp = sc.nextLine();
            String minVersion = new Solution().getMinVersion(temp.split(","));
            System.out.println(minVersion);

        }
    }
}