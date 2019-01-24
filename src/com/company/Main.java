package com.company;

import java.util.concurrent.*;

public class Main {

    private static class LongRunningTask implements Runnable {
        private final long stopTimestamp;

        public LongRunningTask(long stopTimestamp) {
            this.stopTimestamp = stopTimestamp;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000L);
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
                Thread.currentThread().interrupt();
            }
            while(System.currentTimeMillis() < stopTimestamp)
            {
                if (Thread.currentThread().isInterrupted())
                {
                    break;
                }
                //System.out.println(System.currentTimeMillis());
            }
            System.out.println("The task is finished :" + (stopTimestamp - System.currentTimeMillis()));
        }
    };


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(new LongRunningTask(System.currentTimeMillis() + 20000L));
        Thread.sleep(1000L);
        System.out.println("Canceling the future");
        future.cancel(true);
    }
}
