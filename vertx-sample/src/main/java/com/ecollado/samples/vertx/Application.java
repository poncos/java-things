package com.ecollado.samples.vertx;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class Application {

    public static void main(String args[]) {

        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

        JsonObject config = new JsonObject().put("name", "tim").put("directory", "/blah");
        //DeploymentOptions options = new DeploymentOptions().setInstances(16).setConfig(config);
        Verticle httpVerticle = new HttpVerticle();
        vertx.deployVerticle(httpVerticle);

    }
}
