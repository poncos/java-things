package com.ecollado.samples.vertx.jobrunner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ImeiInfo implements Serializable {

    @JsonProperty("imei")
    private String imei;

    @JsonProperty
    private boolean isBlack;

    @JsonProperty
    private boolean isWhite;

    @JsonProperty
    private boolean isGrey;

    public ImeiInfo(String imei, boolean isBlack, boolean isWhite, boolean isGrey) {
        this.imei = imei;
        this.isBlack = isBlack;
        this.isWhite = isWhite;
        this.isGrey = isGrey;
    }

    @Override
    public String toString() {
        return "ImeiInfo{" +
                "imei='" + imei + '\'' +
                ", isBlack=" + isBlack +
                ", isWhite=" + isWhite +
                ", isGrey=" + isGrey +
                '}';
    }
}
