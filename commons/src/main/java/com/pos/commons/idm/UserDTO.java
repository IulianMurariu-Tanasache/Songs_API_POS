package com.pos.commons.idm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password; //TODO: should be encrypted when received
    private Set<String> roles = null;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
