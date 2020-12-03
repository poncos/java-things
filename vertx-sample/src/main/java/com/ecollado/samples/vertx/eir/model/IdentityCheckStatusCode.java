package com.ecollado.samples.vertx.eir.model;

public enum IdentityCheckStatusCode {
    WHITE(0),
    BLACK(1),
    GREY(2),
    UNKNOWN(3)
    ;

    private int value;

    IdentityCheckStatusCode(int aValue) {
        this.value = aValue;
    }

    public int getValue() {
        return this.value;
    }
}
