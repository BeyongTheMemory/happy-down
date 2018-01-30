package com.top.mini.happy.down.util;

/**
 * @author xugang on 18/1/29.
 */

public class CountUtil {

    /**
     * 获取计数器
     * @return 线程不安全的计数器
     */
    public static Count getCount(int num){
        return new Count(num);
    }

    public static class Count{
        private int num;

        public Count(int num) {
            this.num = num;
        }

        public void add(){
            num++;
        }

        public void decrement(){
            num--;
        }

        public int getNum(){
            return num;
        }
    }
}
