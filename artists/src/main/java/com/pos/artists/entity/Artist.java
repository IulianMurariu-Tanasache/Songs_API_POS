package com.pos.artists.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "artists")
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    private String uuid;

    private String name;
    private boolean active = true;
}
