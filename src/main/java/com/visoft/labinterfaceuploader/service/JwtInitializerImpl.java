package com.visoft.labinterfaceuploader.service;

import com.visoft.labinterfaceuploader.dto.LoginOutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
public class JwtInitializerImpl implements JwtInitializer {

    @Value("${app.serverHost}")
    private String serverHost;

    private final RestTemplate restTemplate;
    private final RequestEntityProducer requestEntityProducer;

    private String jwt = null;


    @Override
    public synchronized String getJwt() {
        if (jwt == null) {
            jwt = ofNullable(restTemplate.postForEntity(serverHost + "/graphql", requestEntityProducer.loginRequestEntity(), LoginOutDto.class).getBody())
                    .map(LoginOutDto::getData)
                    .map(LoginOutDto.Login::getLogin)
                    .orElseThrow(() -> new RuntimeException("Fetching JWL failed"));
        }
        return jwt;
    }
}
