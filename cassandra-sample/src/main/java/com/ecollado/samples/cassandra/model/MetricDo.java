package com.ecollado.samples.cassandra.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class MetricDo {

    @PartitionKey
    @Column(name = "deviceId")
    private UUID deviceId;

    @PartitionKey
    @Column(name = "date")
    private Date date;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "reading")
    private String reading;
}
