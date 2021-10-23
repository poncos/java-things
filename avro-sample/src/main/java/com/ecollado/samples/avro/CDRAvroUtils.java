package com.ecollado.samples.avro;

import com.ecollado.sample.avro.CallDetailRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CDRAvroUtils {

    public byte[] serealizeAvroCdrRecord(
            CallDetailRecord cdrRecord) {

        DatumWriter<CallDetailRecord> writer = new SpecificDatumWriter<>(
                CallDetailRecord.class);
        byte[] data = new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encoder jsonEncoder = null;
        try {
            jsonEncoder = EncoderFactory.get().jsonEncoder(
                    CallDetailRecord.getClassSchema(), stream);
            writer.write(cdrRecord, jsonEncoder);
            jsonEncoder.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            //TODO
            //logger.error("Serialization error:" + e.getMessage());
        }
        return data;
    }

    public CallDetailRecord deSerealizeCDR(byte[] data) {
        DatumReader<CallDetailRecord> reader
                = new SpecificDatumReader<>(CallDetailRecord.class);
        Decoder decoder = null;
        try {
            decoder = DecoderFactory.get().jsonDecoder(
                    CallDetailRecord.getClassSchema(), new String(data));
            return reader.read(null, decoder);
        } catch (IOException e) {
            //logger.error("Deserialization error:" + e.getMessage());
        }
        return null;
    }
}
