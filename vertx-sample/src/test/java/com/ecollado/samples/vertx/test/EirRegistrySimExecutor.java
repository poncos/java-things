package com.ecollado.samples.vertx.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EirRegistrySimExecutor<T extends Thread> {

    private static final int POOL_SIZE = 100;

    private ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);


    public EirRegistrySimExecutor() {

    }

    public void execute( T task) {
        this.executorService.submit(task);
    }
}
