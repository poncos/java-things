package com.ecollado.samples.avro;

import com.ecollado.sample.avro.CallDetailRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CDRAvroUtils {

    public byte[] serializeJsonAvroCdrRecord(
            CallDetailRecord cdrRecord) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Encoder jsonEncoder = EncoderFactory.get().jsonEncoder(
                    CallDetailRecord.getClassSchema(), stream);

            return this.serializeAvroCdrRecord(cdrRecord, jsonEncoder, stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] serializeBinaryAvroCdrRecord(
            CallDetailRecord cdrRecord) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encoder jsonEncoder = EncoderFactory.get().binaryEncoder(
                stream, null);

        return this.serializeAvroCdrRecord(cdrRecord, jsonEncoder, stream);
    }

    public CallDetailRecord deserializeJsonAvroCdrRecord(byte[] data) {

        try {
            Decoder jsonDecoder = DecoderFactory.get().jsonDecoder(
                    CallDetailRecord.getClassSchema(), new String(data));

            return this.deserializeRecord(jsonDecoder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public CallDetailRecord deserializeBinaryAvroCdrRecord(byte[] data) {

        Decoder binaryDecoder = DecoderFactory.get().binaryDecoder(data, null);
        return this.deserializeRecord(binaryDecoder);
    }

    private byte[] serializeAvroCdrRecord(
            CallDetailRecord cdrRecord, Encoder encoder, ByteArrayOutputStream stream) {
        DatumWriter<CallDetailRecord> writer = new SpecificDatumWriter<>(
                CallDetailRecord.class);
        byte[] data = new byte[0];

        try {
            writer.write(cdrRecord, encoder);
            encoder.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private CallDetailRecord deserializeRecord(Decoder decoder) {
        DatumReader<CallDetailRecord> reader
                = new SpecificDatumReader<>(CallDetailRecord.class);
        try {
            return reader.read(null, decoder);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
