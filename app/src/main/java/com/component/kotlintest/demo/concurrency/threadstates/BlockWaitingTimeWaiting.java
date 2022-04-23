package com.component.kotlintest.demo.concurrency.threadstates;

/**
 * @Author fox
 * @Date 2022/4/23 22:58
 * 示范 Block、Waiting、TimeWaiting状态
 * 因为kotlin中没有wait()、notify()、notifyAll()方法 故采用java
 */
public class BlockWaitingTimeWaiting implements Runnable{

    @Override
    public void run() {
        syn();
    }

    private synchronized void syn() {
        try {
            Thread.sleep(1000);
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
