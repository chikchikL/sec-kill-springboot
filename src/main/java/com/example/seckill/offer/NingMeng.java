package com.example.seckill.offer;

import java.text.NumberFormat;
import java.util.Scanner;

public class NingMeng {

    int getZeroCount(int d){
        double v = Math.pow(10, d) - Math.pow(10, d-1) - Math.pow(9, d);
        return (int) v;
    }

    //获取最接近180的夹角值
    float getMaxDegree(int n,float[] arr){
        int start = 0;
        float max = 0.0f;
        //每轮找到arr[l]+180最接近的两个值，对比正向夹角大还是反向夹角大
        for(;start < n;++start){
            int idx = getFirstGreaterThan180(start, arr);
            //边界
            if(idx == n){
                idx = 0;
            }
            //比较idx和idx-1与start的夹角哪个更接近180
            float t;
            float d1 = (t = Math.abs(arr[idx] - arr[start])) <= 180 ? t:360-t;
            float d2 = (t = Math.abs(arr[(idx - 1 + n) % n]- arr[start])) <= 180 ?t:360-t;
            float temp = Math.max(d1,d2);
            max = Math.max(temp,max);
        }
        return max;
    }

    //二分法
    //对于每个l指定的元素，找到其顺时针旋转arr[l]+180°的元素，如果没有找到其顺时针旋转的下一个元素的下标
    //边界问题：如果当前找的tar<所有元素，会走到0，如果tar>所有元素,会走到length
    int getFirstGreaterThan180(int start,float[] arr){
        float tar = (arr[start] + 180) % 360;
        int l = 0,r = arr.length;
        while(l<r){
            int mid = (l+r)/2;
            if(arr[mid] >= tar){
                r = mid;
            }else{
                l = mid + 1;
            }
        }
        return l;
    }
    
    
    public static void main(String[] args){
        float[] floats = {1,23.5f,179,180.1f,190.2f};
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){
            int n = sc.nextInt();
            float[] arr = new float[n];
            for(int j = 0;j<n;++j){
                arr[j] = sc.nextFloat();
            }
            float maxDegree = new NingMeng().getMaxDegree(n, arr);
            NumberFormat instance = NumberFormat.getNumberInstance();
            instance.setMaximumFractionDigits(1);
            String res = instance.format(maxDegree);
            System.out.println(res);
        }

    }
}
