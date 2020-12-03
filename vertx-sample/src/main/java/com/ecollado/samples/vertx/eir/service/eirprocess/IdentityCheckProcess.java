package com.ecollado.samples.vertx.eir.service.eirprocess;

import com.ecollado.samples.vertx.eir.model.IdentityCheckRequest;
import com.ecollado.samples.vertx.eir.model.IdentityCheckResponse;
import com.ecollado.samples.vertx.eir.model.IdentityCheckResultCode;
import com.ecollado.samples.vertx.eir.model.IdentityCheckStatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.eventbus.Message;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IdentityCheckProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityCheckProcess.class);

    private final PgPool databasePool;

    private final Message<Object> message;

    public IdentityCheckProcess(PgPool aDatabasePool, Message<Object> aMessage) {
        this.databasePool = aDatabasePool;
        this.message = aMessage;
    }

    public void process(IdentityCheckRequest request) {

        this.databasePool.getConnection(ar -> {

            if (ar.succeeded()) {
                LOGGER.info("connected to database");
                // Obtain our connection
                SqlConnection conn = ar.result();
                // All operations execute on the same connection
                this.checkImei(request, conn);
            } else {
                LOGGER.error("could not connect to database", ar.cause());
                this.replyError(ar.cause());
            }
        });
    }

    private void replySuccess(IdentityCheckStatusCode deviceStatus) {

        IdentityCheckResponse response = new IdentityCheckResponse();
        switch (deviceStatus) {
            case UNKNOWN:
                response.setResultCode(IdentityCheckResultCode.DIAMETER_UNKNOWN);
                break;
            default:
                response.setResultCode(IdentityCheckResultCode.DIAMETER_SUCCESS);
                response.setEquipmentStatus(deviceStatus);
                break;
        }

        try {
            this.message.reply(response.toJsonStr());
        } catch(JsonProcessingException jpe) {
            LOGGER.error("Error sending response.", jpe);
        }

    }

    private void replyError(Throwable throwable) {

        IdentityCheckResponse errorRsp = new IdentityCheckResponse();
        errorRsp.setResultCode(IdentityCheckResultCode.DIAMETER_ERROR);
        errorRsp.setError(throwable.getMessage());

        try {
            this.message.reply(errorRsp.toJsonStr());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error sending response", e);
        }
    }

    private void checkImei(IdentityCheckRequest request, SqlConnection conn) {
        LOGGER.info("Checking IMEI");
        conn.preparedQuery("SELECT status FROM imei WHERE imei=$1")
                .execute(Tuple.of(request.getImei()),ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> rows = ar.result();
                        if (rows.rowCount() > 0) {
                            Row row = rows.iterator().next();
                            Integer status = row.getInteger("status");
                            if (IdentityCheckStatusCode.BLACK.getValue() == status) {
                                this.replySuccess(IdentityCheckStatusCode.BLACK);
                                conn.close();
                            }
                        } else {
                            this.checkImsi(request, conn);
                        }
                    } else {
                        this.replyError(ar.cause());
                        LOGGER.error("Error processing request", ar.cause());
                        conn.close();
                    }
                });
    }

    private void checkImsi(IdentityCheckRequest request, SqlConnection conn) {
        conn.preparedQuery("SELECT status FROM imsi WHERE imsi=$1")
                .execute(Tuple.of(request.getImsi()),ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> rows = ar.result();
                        if (rows.rowCount() > 0) {
                            Row row = rows.iterator().next();
                            Integer status = row.getInteger("status");
                            if (IdentityCheckStatusCode.BLACK.getValue() == status) {
                                this.replySuccess(IdentityCheckStatusCode.BLACK);
                                conn.close();
                                return;
                            }

                        }
                        String msisdn = request.getMsisdn();
                        if (msisdn != null && !msisdn.isEmpty()) {
                            this.checkMsisdn(request, conn);
                        } else {
                            conn.close();
                            this.replySuccess(IdentityCheckStatusCode.UNKNOWN);
                        }
                    } else {
                        this.replyError(ar.cause());
                        LOGGER.error("Error processing request", ar.cause());
                        conn.close();
                    }
                });
    }

    private void checkMsisdn(IdentityCheckRequest request, SqlConnection conn) {
        conn.preparedQuery("SELECT msisdn,status FROM imsi_msisdn WHERE imsi=$1 and msisdn=$2")
                .execute(Tuple.of(request.getImsi(), request.getMsisdn()), ar -> {
                    // Release the connection to the pool
                    if (ar.succeeded()) {
                        RowSet<Row> rows = ar.result();
                        if (rows.rowCount() > 0) {
                            Row row = rows.iterator().next();
                            Integer status = row.getInteger("status");
                            if (IdentityCheckStatusCode.BLACK.getValue() == status) {
                                this.replySuccess(IdentityCheckStatusCode.BLACK);
                                LOGGER.info("closing connection");
                                conn.close();
                                return;
                            }
                        }
                        this.replySuccess(IdentityCheckStatusCode.UNKNOWN);
                        LOGGER.info("closing connection");
                        conn.close();
                    } else {
                        this.replyError(ar.cause());
                        LOGGER.error("Error processing request", ar.cause());
                        conn.close();
                    }
                });
    }
}
