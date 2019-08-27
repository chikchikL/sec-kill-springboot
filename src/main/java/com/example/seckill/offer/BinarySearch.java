package com.example.seckill.offer;


import java.util.LinkedList;
import java.util.PriorityQueue;

//二分查找
public class BinarySearch {

    //找到有序数组第一个元素与下标不同的下标
    int findAbsence(int[] arr,int n){
        //r设置为len代替如果缺失的是最后一个数字的情况
        int mid,l = 0,r = n-1;
        while(l < r){
            mid = (l+r)/2;
            //直接返回机制，即mid元素的左边一个元素为arr[mid] - 2
            if(arr[mid] != mid && mid > 0 && arr[mid-1] == arr[mid] - 2)
                return mid;
            if(arr[mid] > mid){
                //缺失为0则一直走到0
                r = mid;
            }else if(arr[mid] == mid){
                //缺失为len-1则一直走到len-1
                l = mid + 1;
            }
        }
        return l;
    }

    //找到单调递增序列中下标与元素相同的那个元素
    int findIdentical(int [] arr){
        int mid,l = 0,r = arr.length;
        //这里是<=,因为需要判断l和r相遇时那个元素是否满足
        //如果需要返回的是下标而不是元素本身，则不需要判断=的情况
        while(l<=r){
            mid = (l+r)/2;
            if(arr[mid] == mid)
                return mid;
            if(arr[mid] > mid){
                //元素大于下标，递增，后面元素都大于下标
                r = mid - 1;
            }else{
                //元素小于下标，右边
                l = mid + 1;
            }
        }


        //可能不存在
        return -1;
    }

    //0~n-1范围，长度n-1的递增数组，找到那个缺失的数字
    public static void main(String[] args){


//        int[] arr = {1, 2, 3, 4, 5, 6, 7};
//
//        int absence = new BinarySearch().findAbsence(arr,arr.length + 1);
//        System.out.println("缺失数字为:"+absence);
//
//

        int[] arr = {-3,-1,1,3,5};
        int identical = new BinarySearch().findIdentical(arr);
        System.out.println("与下标相同的数值为:"+identical);
    }


}
