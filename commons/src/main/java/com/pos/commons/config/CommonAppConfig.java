package com.pos.commons.config;

import com.pos.commons.dto.SongCreateDTO;
import com.pos.commons.entity.Artist;
import com.pos.commons.entity.Song;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class CommonAppConfig {

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.createTypeMap(SongCreateDTO.class, Song.class).setConverter(context -> {
            Song newSong = new Song();
            newSong.setType(context.getSource().getType());
            newSong.setName(context.getSource().getName());
            newSong.setGenre(context.getSource().getGenre());
            newSong.setReleaseYear(context.getSource().getReleaseYear());

            if(context.getSource().getArtistsUUID() == null) {
                return newSong;
            }
            Set<Artist> artists = new HashSet<>();
            for(String uuid : context.getSource().getArtistsUUID()) {
                Artist newArtist = new Artist();
                newArtist.setUuid(uuid);
                artists.add(newArtist);
                System.out.println(uuid);
            }
            newSong.setArtistSet(artists);
            return newSong;
        });

        return mapper;
    }
}
