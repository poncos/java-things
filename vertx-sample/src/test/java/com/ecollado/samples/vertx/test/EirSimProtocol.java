package com.ecollado.samples.vertx.test;

import com.ecollado.samples.vertx.eir.model.ImeiInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ConcurrentHashMap;

public class EirSimProtocol {

    private static final Logger LOGGER = LoggerFactory.getLogger(EirSimProtocol.class);

    private static ConcurrentHashMap<String, ImeiInfo> IMEI_DATA;

    static {
        IMEI_DATA = new ConcurrentHashMap<>();
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111112",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111113",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111114",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111115",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111116",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111117",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111118",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111119",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111110",false, false, true));
        IMEI_DATA.put("111111111111111",
                new ImeiInfo("111111111111120",false, false, true));
    }

    public ImeiInfo processInput(String input) {

        String[] params = input.split(" ");

        if (params.length != 2) {
            throw new IllegalArgumentException("Wrong format");
        }

        if (params[0] == null || params[1] == null) {
            throw new IllegalArgumentException("Mandatory parameters missing");
        }

        if (params[0].trim().equalsIgnoreCase("GET_IMEI")) {
            String imei = params[1].trim();

            LOGGER.debug("Looking up IMEI value [{}]", imei);

            if (EirSimProtocol.IMEI_DATA.containsKey(imei)) {
                ImeiInfo imeiInfo = EirSimProtocol.IMEI_DATA.get(imei);

                return imeiInfo;
            } else {
                return new ImeiInfo("",false,false, false);
            }
        } else {
            throw new IllegalArgumentException("Wrong Command");
        }
    }
}
