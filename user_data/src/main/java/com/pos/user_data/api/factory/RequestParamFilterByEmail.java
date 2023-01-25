package com.pos.user_data.api.factory;

import com.pos.user_data.entity.UserData;
import com.pos.user_data.service.UserDataService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RequestParamFilterByEmail implements RequestParamFilter{

    private UserDataService service;

    @Override
    public List<UserData> getFilteredList(String valueToFilterBy, Integer page, Integer itemsPerPage) {
        return service.getAllUsersDataByEmail(valueToFilterBy, page, itemsPerPage);
    }
}
