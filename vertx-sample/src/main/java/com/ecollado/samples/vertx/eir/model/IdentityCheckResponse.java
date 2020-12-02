package com.ecollado.samples.vertx.eir.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**IdentityCheckAnswer as specified in TS 129 272.
 * Ex: https://www.etsi.org/deliver/etsi_ts/129200_129299/129272/13.07.00_60/ts_129272v130700p.pdf
 * */
@Getter
@Setter
public class IdentityCheckResponse {

    @JsonProperty
    private IdentityCheckResultCode resultCode;

    @JsonProperty
    private IdentityCheckStatusCode equipmentStatus;

    public static IdentityCheckResponse fromBytes(final byte[] data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        IdentityCheckResponse identityCheckRequest = mapper.readValue(data, IdentityCheckResponse.class);

        return identityCheckRequest;
    }

    public String toJsonStr() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
