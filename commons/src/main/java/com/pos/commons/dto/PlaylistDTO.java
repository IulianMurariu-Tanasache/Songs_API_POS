package com.pos.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlaylistDTO extends RepresentationModel<PlaylistDTO> {

    private String id;
    private Integer userId;
    private String name;
    private Set<SongObject> songs;
}
