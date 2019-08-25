package com.example.seckill.offer;

import java.util.*;


public class Main {

    public static int res ;
    public static PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2.compareTo(o1));
    public static PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    public static void main(String[] args){
        //实际是一个乱序数组，分段后分别排序，使得整体有序，求最大分组数量
        //如果在某个元素位置能分段，那么其左右两边的数字满足左小右大的BST构造规则
        //将数组分别用一个大顶堆和小顶堆容器来动态维护，
        // maxHeap维护已经遍历的左半边元素，minHeap维护剩余待遍历元素
        //小顶堆初始化拥有全部元素，大顶堆为空
        //遍历数组，每轮往大顶堆插入元素，小顶堆移除指定元素
        //判断目前大顶堆的max元素是否<=小顶堆的min元素，如果成立，
        // 代表该元素可以作为分组界限（包含该元素本身）

        int[] arr;
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){

            res = 0;
            maxHeap.clear();
            minHeap.clear();

            int len = sc.nextInt();

            if(len == 0)
                System.out.println(0);

            arr = new int[len];
            for(int i=0;i<len;++i){
                arr[i] = sc.nextInt();
                //初始化小根堆
                minHeap.add(arr[i]);
                System.out.println(minHeap);
            }

            trace(arr,len);

            System.out.println(res);
        }
    }

    //遍历数组
    public static void trace(int[] arr, int len){
        for(int i = 0;i<len-1;++i){
            maxHeap.add(arr[i]);
            minHeap.remove(arr[i]);


            Integer max = maxHeap.peek();
            Integer min = minHeap.peek();

            if(max <= min){
                ++res;
            }
        }


    }
}
