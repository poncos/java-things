package com.ecollado.samples.cassandra.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;
import java.util.UUID;

@Table(name = "device")
public class DeviceDo {

    @PartitionKey
    @Column(name = "id")
    private UUID id;

    @Column(name = "user")
    private UUID user;

    @Column(name = "type")
    private DeviceType type;

    @Column(name = "registered_date")
    private Date registeredDate;


}
