package com.ecollado.samples.avro;

import com.ecollado.sample.avro.CallDetailRecord;
import com.ecollado.sample.avro.CallType;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CDRAvroTest {

    @Test
    public void serializeWithJsonEncoder(){
        CDRAvroUtils serealizer = new CDRAvroUtils();
        byte[] data = serealizer.serializeJsonAvroCdrRecord(this.getCDRRecord());

        System.out.println("----> " + new String(data));
        assertTrue(Objects.nonNull(data));
        assertTrue(data.length > 0);
    }

    @Test
    public void serializeWithBinaryEncoder(){
        CDRAvroUtils serealizer = new CDRAvroUtils();
        byte[] data = serealizer.serializeBinaryAvroCdrRecord(this.getCDRRecord());

        System.out.println("----> " + new String(data));
        assertTrue(Objects.nonNull(data));
        assertTrue(data.length > 0);
    }

    @Test
    public void deserializeWithJsonEncoder(){
        CDRAvroUtils serealizer = new CDRAvroUtils();
        CallDetailRecord actualRecord = this.getCDRRecord();
        byte[] data = serealizer.serializeJsonAvroCdrRecord(actualRecord);
        CallDetailRecord desearializedRecord = serealizer
                .deserializeJsonAvroCdrRecord(data);
        assertEquals(desearializedRecord,actualRecord);
        assertTrue(desearializedRecord.getOriginatingNumber().toString()
                .equals(actualRecord.getOriginatingNumber().toString()));
    }

    @Test
    public void deserializeWithBinaryEncoder(){
        CDRAvroUtils serealizer = new CDRAvroUtils();
        CallDetailRecord actualRecord = this.getCDRRecord();
        byte[] data = serealizer.serializeBinaryAvroCdrRecord(actualRecord);

        System.out.print("Encoded data " + new String(data));
        CallDetailRecord desearializedRecord = serealizer
                .deserializeBinaryAvroCdrRecord(data);
        assertEquals(desearializedRecord,actualRecord);
        assertTrue(desearializedRecord.getOriginatingNumber().toString()
                .equals(actualRecord.getOriginatingNumber().toString()));
    }

    private CallDetailRecord getCDRRecord() {
        return new CallDetailRecord(
                "778987654",
                "987543218",
                "2021-10-08",
                65,
                "7876543456789876",
                "345678123456789",
                CallType.Voice
        );
    }
}
