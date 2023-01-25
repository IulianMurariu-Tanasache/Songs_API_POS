package com.pos.commons.dto;

import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO extends RepresentationModel<SongDTO> {

    private Integer id;
    private String name;
    private SongGenre genre;
    private MusicType type;
    private Integer releaseYear;
    private Set<ArtistDTO> artistSet;
}
