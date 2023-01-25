package com.pos.user_data.service;

import com.pos.user_data.entity.UserData;

import java.util.List;
import java.util.Optional;

public interface UserDataService {

    List<UserData> getAllUsersData(Integer page, Integer itemsPerPage);
    UserData saveUserData(UserData userData);
    Optional<UserData> getUserDataByUserId(Integer userId);
    void deleteUserDataByUserId(Integer userId);
    Optional<UserData> updateUserDataByUserId(Integer userId, UserData userData);
    List<UserData> getAllUsersDataByName(String name, Integer page, Integer itemsPerPage);
    List<UserData> getAllUsersDataByUsername(String valueToFilterBy, Integer page, Integer itemsPerPage);
    List<UserData> getAllUsersDataByEmail(String valueToFilterBy, Integer page, Integer itemsPerPage);
}
