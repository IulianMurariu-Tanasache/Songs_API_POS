package com.pos.commons.api;

import com.pos.commons.dto.UserDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/userdata")
public interface UserDataAPI {

    @GetMapping
    ResponseEntity<List<UserDataDTO>> getAllUserData(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer page,
            @RequestParam(value = "items_per_page", defaultValue = "10") Integer itemsPerPage
    );

    @PostMapping
    ResponseEntity<Void> addUserData(@RequestBody UserDataDTO userDataDTO);

    @GetMapping("/{id}")
    ResponseEntity<UserDataDTO> getUserDataById(@PathVariable Integer id);

    @PutMapping("/{id}")
    ResponseEntity<Void> addOrUpdateUserData(@PathVariable Integer id, @RequestBody UserDataDTO userDataDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUserDataById(@PathVariable Integer id);
}
