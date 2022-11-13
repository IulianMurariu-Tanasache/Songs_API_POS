package com.pos.music.dto;

import com.pos.music.enums.MusicType;
import com.pos.music.enums.SongGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {

    private String name;
    private SongGenre genre;
    private MusicType type;
    private Integer releaseYear;
}
