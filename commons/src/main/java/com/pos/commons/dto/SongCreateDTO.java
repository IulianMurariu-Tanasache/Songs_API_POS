package com.pos.commons.dto;

import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongCreateDTO {

    private String name;
    private SongGenre genre;
    private MusicType type;
    private Integer releaseYear;
    private Set<String> artistsUUID;
}
