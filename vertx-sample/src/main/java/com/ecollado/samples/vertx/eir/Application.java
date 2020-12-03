package com.ecollado.samples.vertx.eir;

import com.ecollado.samples.vertx.eir.service.http.IdentityCheckHttpVerticle;
import com.ecollado.samples.vertx.eir.service.eirprocess.IdentityCheckWorkerVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import java.net.URL;

public class Application {

    private static final boolean CLUSTERED = true;

    public static void main(String args[]) {

        Application app = new Application();

        if (CLUSTERED) {
            Vertx.clusteredVertx(new VertxOptions(), res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    app.deployVerticles(vertx);
                } else {
                    throw new RuntimeException(res.cause());
                }
            });
        } else {
            Vertx vertx = Vertx.vertx();
            app.deployVerticles(vertx);
        }
    }

    public void deployVerticles(Vertx vertx) {
        URL resource = IdentityCheckHttpVerticle.class.getResource("/config.json");
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", resource.getPath()));
        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        retriever.getConfig( ar -> {
            if (ar.failed()) {
                throw new RuntimeException(ar.cause());
            } else {

                int cores = Runtime.getRuntime().availableProcessors();
                JsonObject config = ar.result();
                DeploymentOptions optsHttpVerticle = new DeploymentOptions()
                        .setConfig(config)
                        .setInstances(1);
                DeploymentOptions optsWorkerVerticle = new DeploymentOptions()
                        .setInstances((cores>1) ? cores:1)
                        .setConfig(config);

                vertx.deployVerticle(IdentityCheckWorkerVerticle.class.getCanonicalName(),
                        optsWorkerVerticle);
                vertx.deployVerticle(IdentityCheckHttpVerticle.class.getCanonicalName(),
                        optsHttpVerticle);
            }
        });
    }
}
