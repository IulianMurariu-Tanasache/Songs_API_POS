package com.pos.commons.entity;

import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "music")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Enumerated(EnumType.STRING)
    private SongGenre genre;
    @Enumerated(EnumType.ORDINAL)
    private MusicType type;
    private Integer releaseYear;

    @ManyToMany(
            fetch = FetchType.EAGER, cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "music_artists",
            joinColumns = @JoinColumn(name = "id_music"),
            inverseJoinColumns = @JoinColumn(name = "id_artist")
    )
    private Set<Artist> artistSet;
}
