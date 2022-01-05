package com.visoft.labinterfaceuploader.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginOutDto {

    private Login data;


    @Getter
    @Setter
    public static class Login {

        private String login;
    }
}
