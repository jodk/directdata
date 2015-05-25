package test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by zhangdekun on 15-5-21-下午2:25.
 */
public class ForkJoinTest {
    public static void main(String[] args) {
        int[] arry = new int[1000];
        for(int i=0;i<arry.length;i++){
            arry[i] = i;//(int)(Math.random()*100);
        }
        ForkJoinPool pool = new ForkJoinPool();
        SumCount sum = new SumCount(arry,0,arry.length);
        Long l = pool.invoke(sum);
        pool.shutdown();
        System.out.println("result is :"+l);
    }

}
class SumCount extends RecursiveTask<Long>{
    private int[] arry;
    private int start;
    private int end;
    public SumCount(int[] arry,int start,int end){
        this.arry = arry;
        this.start = start;
        this.end = end;
    }
    @Override
    protected Long compute() {
        if(end-start>100){
            int mid = (end+start)/2;
            SumCount s1 = new SumCount(arry,start,mid);
            SumCount s2 = new SumCount(arry,mid,end);
            s1.fork();
            s2.fork();
            return s1.join()+s2.join();
        }else {
            long sum=0l;
            for(int i=start;i<end;i++){
                sum += arry[i];
            }
            return sum;
        }
    }
}
