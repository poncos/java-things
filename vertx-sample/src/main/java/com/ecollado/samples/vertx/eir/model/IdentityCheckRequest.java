package com.ecollado.samples.vertx.eir.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * IdentityCheckAnswer as specified in TS 129 272.
 * Ex: https://www.etsi.org/deliver/etsi_ts/129200_129299/129272/13.07.00_60/ts_129272v130700p.pdf
 * */
@Getter
@Setter
public class IdentityCheckRequest {

    @JsonProperty("imsi")
    private String imsi;

    @JsonProperty("imei")
    private String imei;

    @JsonProperty("msisdn")
    private String msisdn;


    public static IdentityCheckRequest fromBytes(final byte[] data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        IdentityCheckRequest identityCheckRequest = mapper.readValue(data, IdentityCheckRequest.class);

        return identityCheckRequest;
    }

    public String toJsonStr() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
