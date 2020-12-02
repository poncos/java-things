package com.ecollado.samples.vertx.jobrunner;

import com.ecollado.samples.vertx.service.http.DefaultHttpHandler;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class Application {

    public static void main(String args[]) {

        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));



    }
}
