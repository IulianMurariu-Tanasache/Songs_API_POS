package com.pos.user_data.api.factory;

import com.pos.user_data.service.UserDataService;

import java.util.AbstractMap;


public class RequestParamFilterCreator {

   private boolean verifyOnlyParameterIsNotNull(Object... objects) {
       boolean oneNotNull = false;
       for(Object object : objects) {
           if(object != null) {
               if(!oneNotNull) {
                   oneNotNull = true;
               } else {
                   return false;
               }
           }
       }
       return true;
   }

    public AbstractMap.SimpleEntry<String, String> getParamToFilterBy(String name, String username, String email) {
       if(!verifyOnlyParameterIsNotNull(name,username,email))
           throw new IllegalArgumentException("Wrong request parameters!");
       if(name != null) {
           return new AbstractMap.SimpleEntry<>("name",name);
       }
       if(username != null) {
           return new AbstractMap.SimpleEntry<>("username",username);
       }
       if(email != null) {
           return new AbstractMap.SimpleEntry<>("email",email);
       }
       return new AbstractMap.SimpleEntry<>("none", null);
    }

    public RequestParamFilter getRequestParamFilter(UserDataService service, String param) {
        switch (param.toLowerCase()) {
            case "name":
                return new RequestParamFilterByName(service);
            case "username":
                return new RequestParamFilterByUsername(service);
            case "email":
                return new RequestParamFilterByEmail(service);
            case "none":
                return new RequestParamFilterByNone(service);
            default:
                throw new IllegalArgumentException("Invalid param: " + param);
        }
    }
}
