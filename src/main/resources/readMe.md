1 、线程池相关的

@Async("taskAsyncExecutor"):这个线程池使用指定线程池，异步调用，但是需要跨类调用，不够灵活

2、    @GetMapping("/order") 线程池监控

3、    @GetMapping("/test")  多线程处理大list数据，采用countDownlatch,分段处理list，以后遇到查询数据的，也可以采用
多线程分段查询数据库，再综合拿数据