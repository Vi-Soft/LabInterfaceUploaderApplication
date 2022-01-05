package com.visoft.labinterfaceuploader.service;

import com.visoft.labinterfaceuploader.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.file.Path;

@RequiredArgsConstructor
@Service
public class RequestEntityProducerImpl implements RequestEntityProducer {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;


    @Override
    public HttpEntity<LoginDto> loginRequestEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(new LoginDto(username, password), headers);
    }

    @Override
    public HttpEntity<MultiValueMap<String, Object>> uploadResultsToLabOrderRequestEntity(Path path, String jwtString) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", "Bearer " + jwtString);

        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", new FileSystemResource(path));

        return new HttpEntity<>(body, headers);
    }
}
