package com.jn.langx.util.retry;

public class RetryableRunnable implements Runnable{
    private Runnable task;
    private RetryConfig retryConfig = new RetryConfig();
    /**
     * 已尝试次数
     */
    private int attempts;

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void addAttempts(){
        this.addAttempts(1);
    }

    public void addAttempts(int count){
        this.attempts = this.attempts + count;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public Runnable getTask() {
        return task;
    }

    public RetryConfig getRetryConfig() {
        return retryConfig;
    }

    public void setRetryConfig(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

    @Override
    public void run() {
        if(this.retryConfig==null){
            this.retryConfig  = new RetryConfig();
        }
        if(this.attempts<retryConfig.getMaxAttempts()){
            this.task.run();
        }
    }
}
