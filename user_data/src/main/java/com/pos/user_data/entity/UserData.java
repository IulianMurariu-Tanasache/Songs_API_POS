package com.pos.user_data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "personal_data")
public class UserData {
    @Id
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private Date birthDate; // see if better as string or date
}
