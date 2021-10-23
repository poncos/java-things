package com.ecollado.samples.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

public class CreateAvroSchemaApp {

    public static void main(String args[]) {
        Schema clientIdentifier = SchemaBuilder.record("CallDetailRecord")
                .namespace("com.ecollado.sample.avro")
                .fields()
                    .requiredString("originatingNumber")
                   .requiredString("terminatingNumber")
                   .requiredString("startingTime")
                   .optionalInt("duration")
                   .optionalString("imsi")
                   .optionalString("imei")
                   .name("callType").type().enumeration("CallType")
                   .symbols("Voice","Text","Data")
                   .enumDefault("Voice")
                .endRecord();

        System.out.println(clientIdentifier.toString());
    }
}
