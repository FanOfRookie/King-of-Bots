package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{

    private final static ReentrantLock lock= new ReentrantLock ();

    //信号量
    private final Condition condition = lock.newCondition();

    private final Queue<Bot> bots = new LinkedList<>();

    public void addBot (Integer userId,String botCode, String input){
        lock.lock();
        try {
            bots.add(new Bot(userId,botCode,input));
            condition.signal();
        }finally {
            lock.unlock();
        }
    }

    //代码执行，比较耗时
    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);
    }

    //生产者-消费者模型的消息队列
    @Override
    public void run() {
        while (true){
            lock.lock();
            //待执行代码队列为空
            if(bots.isEmpty()){
                try {
                    //阻塞线程,condition与lock绑定，当condition被释放，则会默认调用unlock以释放锁
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else {
                Bot bot = bots.poll();
                lock.unlock();
                consume(bot);
            }
        }
    }
}
