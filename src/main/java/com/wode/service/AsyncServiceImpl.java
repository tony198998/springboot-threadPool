package com.wode.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Async("taskAsyncExecutor") //注：该注解可加在方法上，表示启用该线程池异步调用，异步调用：只有其他类调该类的某方法，调用底层cglib，动态生产代理子类，本类A调用B，只会方法内调用，走的是A的线程
@Service
public class AsyncServiceImpl implements AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Resource(name = "taskAsyncExecutor")
    public Executor threadPool;


    @Override
    public void executeAsync() {
        logger.info("start executeAsync");
        System.out.println("异步线程1名称：" + Thread.currentThread().getName());
        axync2();
        logger.info("end executeAsync");
    }

    @Override
    public void axync2() {
        System.out.println("异步线程2名称：" + Thread.currentThread().getName());
    }

}