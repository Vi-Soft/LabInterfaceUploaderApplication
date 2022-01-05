package com.visoft.labinterfaceuploader.service;

import org.springframework.http.HttpStatus;

import java.nio.file.Path;

public interface FileMover {

    void prepareFolders();

    void move(HttpStatus httpStatus, Path file);
}
