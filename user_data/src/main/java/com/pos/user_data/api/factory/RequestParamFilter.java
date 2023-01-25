package com.pos.user_data.api.factory;

import com.pos.user_data.entity.UserData;

import java.util.List;

public interface RequestParamFilter {
    List<UserData> getFilteredList(String valueToFilterBy, Integer page, Integer itemsPerPage);
}
