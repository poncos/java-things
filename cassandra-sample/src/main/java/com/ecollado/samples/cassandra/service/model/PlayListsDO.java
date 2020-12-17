package com.ecollado.samples.cassandra.service.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ecollado on 4/11/15.
 */
@Table(keyspace = "music", name = "playlists")
public class PlayListsDO
{
    @PartitionKey
    private UUID id;

    @ClusteringColumn(value=0)
    private int song_order;

    @Column
    private String album;

    @Column
    private String artist;

    @Column
    private List<String> reviews;

    @Column
    private UUID song_id;

    @Column
    private Set<String> tags;

    @Column
    private String title;

    @Column
    private Map<Timestamp, String> venue;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getSong_order() {
        return song_order;
    }

    public void setSong_order(int song_order) {
        this.song_order = song_order;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public UUID getSong_id() {
        return song_id;
    }

    public void setSong_id(UUID song_id) {
        this.song_id = song_id;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Timestamp, String> getVenue() {
        return venue;
    }

    public void setVenue(Map<Timestamp, String> venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        return "PlayListsDO{" +
                "id=" + id +
                ", song_order=" + song_order +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", reviews=" + reviews +
                ", song_id=" + song_id +
                ", tags=" + tags +
                ", title='" + title + '\'' +
                ", venue=" + venue +
                '}';
    }
}
