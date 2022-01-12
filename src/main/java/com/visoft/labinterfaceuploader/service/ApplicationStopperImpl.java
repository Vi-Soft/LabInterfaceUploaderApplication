package com.visoft.labinterfaceuploader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplicationStopperImpl implements ApplicationStopper {

    @Value("${app.stopAfterFirstJob}")
    private boolean stopAfterFirstJob;

    private final ConfigurableApplicationContext applicationContext;


    @Override
    public void checkStopAfterFirstJobCondition() {
        if (stopAfterFirstJob) {
            stop(0);
        }
    }

    @Override
    public void stop(int exitCode) {
        System.exit(SpringApplication.exit(applicationContext, () -> exitCode));
    }
}
