package com.ecollado.samples.cassandra.service;

import com.datastax.driver.core.*;
import com.ecollado.samples.cassandra.service.daos.PlayListDatastaxDAO;
import com.ecollado.samples.cassandra.service.model.PlayListsDO;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ecollado on 4/7/15.
 */
public class DatastaxTestApp
{
    // cluster.getConfiguration().getPoolingOptions().setMaxConnectionsPerHost(HostDistance.LOCAL,100);
    private Cluster cluster;

    public void connect(String[] nodes)
    {
        cluster = Cluster.builder()
                .addContactPoints(nodes)
                .withCredentials("cassandra","cassandra")
                .build();

        cluster.getConfiguration().getPoolingOptions().setMaxConnectionsPerHost(HostDistance.LOCAL,20);

        int maxCon = cluster.getConfiguration().getPoolingOptions().getMaxConnectionsPerHost(HostDistance.LOCAL);
        System.out.println("MaxConnections: " + maxCon);

        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n",
                metadata.getClusterName());

        for ( Host host : metadata.getAllHosts() )
        {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }
    }

    public Session getSession()
    {
        return this.cluster.connect();
    }

    public void close()
    {
        cluster.close();
    }

    public static void main(String[] args)
    {
        DatastaxTestApp client = new DatastaxTestApp();
        String[] nodes = {"alsafi","aldebaran"};
        client.connect(nodes);

        Session session = client.getSession();
        PlayListDatastaxDAO dao = new PlayListDatastaxDAO(session);

        List<PlayListsDO> playListsDOList = dao.findById("62c36092-82a1-3a00-93d1-46196ee77204");

        System.out.println("List: " + Arrays.toString(playListsDOList.toArray()));

        PlayListsDO pl =
                dao.findByUniqueKey("62c36092-82a1-3a00-93d1-46196ee77204",3);

        System.out.println("PL: " + pl);

        client.close();
    }

}
