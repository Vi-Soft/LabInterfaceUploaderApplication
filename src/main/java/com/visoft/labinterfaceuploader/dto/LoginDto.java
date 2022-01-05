package com.visoft.labinterfaceuploader.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String query;


    public LoginDto(String username, String password) {
        this.query =  "query{ login( dto: { email:\"" + username + "\" password:\"" + password + "\" } )  }";
    }
}
