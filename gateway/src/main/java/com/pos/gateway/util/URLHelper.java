package com.pos.gateway.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

public class URLHelper {

    public static String getFullPath() {
        UriComponents uri = ServletUriComponentsBuilder.fromCurrentRequest().build();
        String path = uri.getPath();
        String queryParams = uri.getQuery();
        return path + (queryParams != null ? "?" + queryParams : "");
    }
}
