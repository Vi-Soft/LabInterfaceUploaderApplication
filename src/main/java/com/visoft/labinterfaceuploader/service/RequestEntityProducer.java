package com.visoft.labinterfaceuploader.service;

import com.visoft.labinterfaceuploader.dto.LoginDto;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

import java.nio.file.Path;

public interface RequestEntityProducer {

    HttpEntity<LoginDto> loginRequestEntity();

    HttpEntity<MultiValueMap<String, Object>> uploadResultsToLabOrderRequestEntity(Path path, String jwtString);
}
