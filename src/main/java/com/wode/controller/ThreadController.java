package com.wode.controller;

import com.wode.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description:
 * @author: jitao
 * @createDate: 2021/7/23
 */
@RestController
@Slf4j
public class ThreadController {


    @Autowired
    private AsyncService asyncService;

    @Resource(name = "taskAsyncExecutor")
    public Executor threadPool;

    @GetMapping("/sss")
    public void sss() {
        //调用service层的任务
        asyncService.executeAsync();
       // asyncService.axync2();
        System.out.println("主线程名称：" + Thread.currentThread().getName());
    }

    private static List<String> list = new ArrayList<>();

    @PostConstruct
    public void init() {
        for (int i = 0; i < 200000; i++) {
            list.add("name:" + i);
        }
    }


    @GetMapping("/test1")
    public void test1() {
        List<String> result = new CopyOnWriteArrayList<>();
        long a = System.currentTimeMillis();
        for (String str : list) {
            result.add(str);
        }
        long b = System.currentTimeMillis();
        System.out.println(result.size());
        System.out.println(b - a);
    }


    @GetMapping("/test")
    public void test() throws InterruptedException {
        int core = 10;
        CountDownLatch latch = new CountDownLatch(core);
        List<String> result = new CopyOnWriteArrayList<>();
        long a = System.currentTimeMillis();
        int ddd = 10000;
        for (int i = 0; i < 10; i++) {
            deal(threadPool, list, ddd, result, latch, i);
        }
        latch.await();
        long b = System.currentTimeMillis();
        System.out.println(result.size());
        System.out.println(b - a);
    }

    private void deal(Executor threadPool, List<String> list, int ddd, List<String> result, CountDownLatch latch, int i) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                List<String> list1 = list.subList(i * ddd, (i + 1) * ddd);
                for (String str : list1) {
                    result.add(str);
                }
                latch.countDown();
            }
        });
    }


    /**
     * 这里我们可以通过接口实时观看效果 具体效果如下图,可以监控线程池状态
     *
     * @return
     */
    @GetMapping("/order")
    public Map getThreadInfo() {
        Map map = new HashMap();
        Object[] myThread = {threadPool};
        for (Object thread : myThread) {
            ThreadPoolTaskExecutor threadTask = (ThreadPoolTaskExecutor) thread;
            ThreadPoolExecutor threadPoolExecutor = threadTask.getThreadPoolExecutor();
            System.out.println("提交任务数" + threadPoolExecutor.getTaskCount());
            System.out.println("完成任务数" + threadPoolExecutor.getCompletedTaskCount());
            System.out.println("当前有" + threadPoolExecutor.getActiveCount() + "个线程正在处理任务");
            System.out.println("还剩" + threadPoolExecutor.getQueue().size() + "个任务");
            map.put("提交任务数-->", threadPoolExecutor.getTaskCount());
            map.put("完成任务数-->", threadPoolExecutor.getCompletedTaskCount());
            map.put("当前有多少线程正在处理任务-->", threadPoolExecutor.getActiveCount());
            map.put("还剩多少个任务未执行-->", threadPoolExecutor.getQueue().size());
            map.put("当前可用队列长度-->", threadPoolExecutor.getQueue().remainingCapacity());
            map.put("当前时间-->", new Date());
        }
        return map;
    }


}