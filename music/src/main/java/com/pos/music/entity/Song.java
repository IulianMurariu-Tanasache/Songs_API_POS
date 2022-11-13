package com.pos.music.entity;

import com.pos.music.enums.MusicType;
import com.pos.music.enums.SongGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
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

    /*@ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "music_artists",
            joinColumns = @JoinColumn(name = "id_music"),
            inverseJoinColumns = @JoinColumn(name = "id_artist")
    )
    private Set<com.pos.music.entity.Artist> artistSet;*/
}
