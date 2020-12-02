package com.ecollado.samples.vertx.jobrunner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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

}
