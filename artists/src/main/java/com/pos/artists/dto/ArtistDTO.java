package com.pos.artists.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO extends RepresentationModel<ArtistDTO> {

    private String uuid;
    private String name;
    private boolean active;
}
