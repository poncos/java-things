package com.ecollado.samples.cassandra.service.daos;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.ecollado.samples.cassandra.service.model.PlayListsDO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

/**
 * Created by ecollado on 4/11/15.
 */
public class PlayListDatastaxDAO
{
    private Session session;

    public PlayListDatastaxDAO(Session session)
    {
       this.session = session;
    }

    public void insert(PlayListsDO playListDO)
    {
        Mapper<PlayListsDO> mapper = new MappingManager(session).mapper(PlayListsDO.class);

        mapper.save(playListDO);
    }

    public PlayListsDO findByUniqueKey(String id, int order)
    {
        Mapper<PlayListsDO> mapper = new MappingManager(session).mapper(PlayListsDO.class);
        PlayListsDO readPlayList = mapper.get(UUID.fromString(id),order);

        return readPlayList;
    }

    public List<PlayListsDO> findById(String id)
    {
        Mapper<PlayListsDO> mapper = new MappingManager(session).mapper(PlayListsDO.class);
        Statement statement = QueryBuilder.select()
                .all()
                .from("music", "playlists")
                .where(eq("id", UUID.fromString("62c36092-82a1-3a00-93d1-46196ee77204")));

        ResultSet results = session.execute(statement);
        Result<PlayListsDO> rsDos = mapper.map(results);
        List<PlayListsDO> playListsDOList = new ArrayList<PlayListsDO>();

        for ( PlayListsDO pl : rsDos )
        {
            playListsDOList.add(pl);
        }

        return playListsDOList;
    }

}
