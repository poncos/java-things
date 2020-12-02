package com.ecollado.samples.vertx.eir.service;

import com.ecollado.samples.vertx.eir.config.DatabaseConfig;
import com.ecollado.samples.vertx.eir.config.IdentityCheckConfig;
import com.ecollado.samples.vertx.eir.model.IdentityCheckRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

import java.io.IOException;
import java.util.UUID;


public class IdentityCheckWorkerVerticle extends AbstractVerticle {

    private final UUID workerId = UUID.randomUUID();

    private long threadId;

    private PgPool pool;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        this.threadId = Thread.currentThread().getId();

        IdentityCheckConfig config  = new IdentityCheckConfig(this.config());
        DatabaseConfig dbConfig     = config.getDatabaseConfig("eir");

        pool = PgPool.pool(vertx, new PgConnectOptions()
                .setPort(dbConfig.getPort())
                .setHost(dbConfig.getHost())
                .setDatabase(dbConfig.getDbname())
                .setUser(dbConfig.getUsername())
                .setPassword(dbConfig.getPassword()), new PoolOptions()
                    .setMaxSize(dbConfig.getMaxPoolSize())
        );

        EventBus eb = vertx.eventBus();
        eb.consumer("eir.identity-check-request", message -> {

            System.out.println("Received message: " + message.body());
            try {
                IdentityCheckRequest identityCheckRequest =
                        IdentityCheckRequest.fromBytes(message.body().toString().getBytes());

                IdentityCheckProcess icProcess = new IdentityCheckProcess(pool, message);
                icProcess.process(identityCheckRequest);
            } catch (IOException e) {
                message.reply("{\"workerId\":\""+this.workerId+"\",\"threadId\":\""+this.threadId+"\"}");
            }
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        pool.close();
    }
}
