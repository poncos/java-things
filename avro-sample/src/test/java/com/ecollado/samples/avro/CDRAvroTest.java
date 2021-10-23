package com.ecollado.samples.avro;

import com.ecollado.sample.avro.CallDetailRecord;
import com.ecollado.sample.avro.CallType;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CDRAvroTest {

    @Test
    public void whenSerialized_UsingJSONEncoder_ObjectGetsSerialized(){
        CDRAvroUtils serealizer = new CDRAvroUtils();
        byte[] data = serealizer.serealizeAvroCdrRecord(this.getCDRRecord());

        assertTrue(Objects.nonNull(data));
        assertTrue(data.length > 0);
    }

    @Test
    public void whenDeserializeUsingJSONDecoder_thenActualAndExpectedObjectsAreEqual(){
        CDRAvroUtils serealizer = new CDRAvroUtils();
        CallDetailRecord actualRecord = this.getCDRRecord();
        byte[] data = serealizer.serealizeAvroCdrRecord(actualRecord);
        CallDetailRecord desearializedRecord = serealizer
                .deSerealizeCDR(data);
        assertEquals(desearializedRecord,actualRecord);
        assertTrue(desearializedRecord.getOriginatingNumber()
                .equals(desearializedRecord.getOriginatingNumber()));
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
