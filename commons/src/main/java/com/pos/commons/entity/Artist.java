package com.pos.commons.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "artists")
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    private String uuid;

    private String name;
    private boolean active = true;

    //@ManyToMany(mappedBy = "artistSet")
    //private Set<Song> songSet;
}
