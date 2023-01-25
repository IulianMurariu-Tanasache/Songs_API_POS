package com.pos.commons.util;

import org.springframework.data.domain.PageRequest;

public class ServiceUtility {

    public static PageRequest makePageRequest(Integer page, Integer itemsPerPage)
    {
        return page == null ?
                PageRequest.of(0, Integer.MAX_VALUE) :
                PageRequest.of(page, itemsPerPage);
    }
}
