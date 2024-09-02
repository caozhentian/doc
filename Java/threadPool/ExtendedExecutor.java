package com.acoinfo.edgerapp.features.job.manager;

import com.acoinfo.edgerapp.base.log.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExtendedExecutor extends ThreadPoolExecutor {
    public static final String TAG = "ExtendedExecutor";

    public ExtendedExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ExtendedExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ExtendedExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ExtendedExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        int tId = android.os.Process.myTid();
        Log.i(TAG, "before Execute:" + tId);
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        int tId = android.os.Process.myTid();
        super.afterExecute(r, t);
        if(t != null) {
            Log.i(TAG, "after Execute:" + tId + ",throwable:" + t);
        } else {
            if(r instanceof FutureTask) {
                try {
                    ((FutureTask<?>) r).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.e(TAG, "submit ExecutionException: $e");
                } catch (InterruptedException e) {
                    Log.e(TAG, "submit InterruptedException: $e");
                }
            }
        }
    }

    @Override
    public void execute(Runnable command) {
        int tId = android.os.Process.myTid();
        Log.i(TAG, "execute:" + tId);
        super.execute(command);
    }
}
