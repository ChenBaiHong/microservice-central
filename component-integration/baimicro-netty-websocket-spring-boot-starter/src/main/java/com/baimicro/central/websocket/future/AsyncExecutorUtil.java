package com.baimicro.central.websocket.future;

import com.baimicro.central.websocket.model.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @ClassName AsyncExecutorUtil
 * @Description TODO Asynchronous executor 异步执行程序工具类
 * @Author baiHoo.chen
 * @Date 2019/11/14 10:54
 */
@Slf4j
@SuppressWarnings("all")
public class AsyncExecutorUtil {


    /***
     *
     * @Author baihoo.chen
     * @Description TODO 启动 两个线程异步,等待有序执行（何意：就是第一个执行完后返回结果时 第二个线程才可以开始执行，但此过程中第一个线程结果对象将会异步返回）程序
     * @Date 2019/11/14 10:59
     * @Param []
     * @return org.apache.poi.ss.formula.functions.T
     **/
    public static <T> T startTowThreadAsyncExecutor(ProgramBlock<T> block1, ProgramBlock<T> block2)
            throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        boolean be = false;
        try {
            CompletableFuture<T> future2 = CompletableFuture.supplyAsync(() -> {
                return block1.executionProgram();
            }, executor);
            be = true;
            return future2.get();
        } finally {
            try {
                if (be) {
                    CompletableFuture<T> future2 = CompletableFuture.supplyAsync(new Supplier<T>() {
                        @Override
                        public T get() {
                            return block2.executionProgram();
                        }
                    }, executor);
                }
            } finally {
                // 释放资源线程
                executor.shutdown();
            }
        }
    }

    /***
     *
     * @Author baihoo.chen
     * @Description TODO  启动多个线程异步执行(暂无可用场景)
     * @Date 2019/11/19 9:45
     * @Param [programBlocks]
     * @return void
     **/
    public static <T> void startMoreThreadAsyncExecutor(List<ProgramBlock<T>> programBlocks) {
        ExecutorService executor = Executors.newFixedThreadPool(programBlocks.size());
        try {
            for (ProgramBlock<T> pb : programBlocks) {
                CompletableFuture.supplyAsync(() -> {
                    return pb.executionProgram();
                }, executor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * @return com.baimicro.central.common.model.Result
     * @Author baihoo.chen
     * @Description TODO 启动多个线程并行执行业务
     * @Date 2019/12/16 14:33
     * @Param [programBlocks]
     **/
    public static <T> Result startMoreThreadParallelExecutor(List<ProgramBlock<T>> programBlocks) {
        List<T> tList = new ArrayList<>();
        final int threadSize = programBlocks.size();
        final CountDownLatch latch = new CountDownLatch(threadSize);
        ExecutorService threadPool = Executors.newFixedThreadPool(threadSize);
        List<Future<T>> futureTaskList = new ArrayList<>(threadSize);
        for (ProgramBlock<T> pb : programBlocks) {
            futureTaskList.add(threadPool.submit(() -> {
                latch.countDown();
                return pb.executionProgram();
            }));
        }
        try {
            latch.await();
            for (Future<T> future : futureTaskList) {
                tList.add(future.get());
                log.info("执行结果：" + future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error("执行错误结果：" + e);
        } finally {
            // 释放资源线程
            threadPool.shutdown();
        }
        return new Result<>(tList);
    }

    public static void main(String[] args) {
        List<ProgramBlock<Integer>> programBlocks = new ArrayList<>();
        programBlocks.add(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12 * 2;
        });
        programBlocks.add(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12 * 3;
        });
        programBlocks.add(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12 * 4;
        });
        programBlocks.add(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12 * 5 * 5 * 65;
        });
        programBlocks.add(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12 * 6;
        });
        programBlocks.add(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12 * 7;
        });
        Long start;
        Long end;
        log.info("当前时间：" + (start = System.currentTimeMillis()));
        startMoreThreadParallelExecutor(programBlocks);
        log.info("结束时间：" + (end = System.currentTimeMillis()));
        log.info("完成时间：" + (end - start));
    }
}
