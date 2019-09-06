package com.example.seckill.offer;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;

import java.util.*;

public class Main{
//    给定一个整数的数组，找出其中的pair(a,  b)，使得a+b=0，
//    并返回这样的pair数目。（a,  b）和(b,  a)是同一组。
    public static void main(String[] args){


        Scanner sc = new Scanner(System.in);

        ArrayList<Integer> arr = new ArrayList<>();
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] splits = line.split(",");

            //输入非法
            if(splits.length == 0)
                System.out.println(0);

            for(String x : splits){
                arr.add(Integer.valueOf(x.trim()));
            }

            Collections.sort(arr);
            //排序后一头一尾两个指针，如果两数之和sum>0,r指针左移，如果sum<0,l指针右移
            //直到相遇
            int pairCount = new Main().findPairCount(arr);
            System.out.println(pairCount);


        }



    }

    private int findPairCount(List<Integer> arr) {
        int l = 0,r = arr.size()-1;
        int sum;

        //需要去重
        HashSet<String> set = new HashSet<>();

        while(l < r){
            sum = arr.get(l) + arr.get(r);
            if(sum == 0){
                set.add(arr.get(l)+"-"+arr.get(r));
                ++l;
                --r;
            }else if(sum < 0){
                ++l;
            }else{
                --r;
            }
        }


        Integer[] ints = {9,8,113,32};

        Arrays.sort(ints,(i1,i2)-> (i1+""+i2).compareTo(i2+""+i1));

        return set.size();
    }
}
