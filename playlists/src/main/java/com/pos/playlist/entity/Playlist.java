package com.pos.playlist.entity;

import com.pos.commons.dto.SongObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "playlists")
public class Playlist {
    @Id
    private String id;

    private Integer userId;
    private String name;
    private Set<SongObject> songs;
}
