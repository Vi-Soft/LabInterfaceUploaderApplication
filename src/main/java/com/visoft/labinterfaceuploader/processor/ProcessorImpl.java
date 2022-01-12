package com.visoft.labinterfaceuploader.processor;

import com.visoft.labinterfaceuploader.service.ApplicationStopper;
import com.visoft.labinterfaceuploader.service.FileMover;
import com.visoft.labinterfaceuploader.service.JwtInitializer;
import com.visoft.labinterfaceuploader.service.RequestEntityProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProcessorImpl implements Processor {

    @Value("${app.labInterfaceFilesPath}")
    private String labInterfaceFilesPath;

    @Value("${app.serverHost}")
    private String serverHost;

    private final RequestEntityProducer requestEntityProducer;
    private final FileMover fileMover;
    private final RestTemplate restTemplate;
    private final JwtInitializer jwtInitializer;
    private final ApplicationStopper applicationStopper;


    @PostConstruct
    void prepare() {
        fileMover.prepareFolders();
    }

    @Scheduled(fixedRateString = "${app.jobFrequency}", timeUnit = TimeUnit.MINUTES)
    @Override
    public void process() {
        log.info("Start processing...");

        for (Path file : files()) {
            fileMover.move(processUploading(file), file);
        }

        applicationStopper.checkStopAfterFirstJobCondition();
    }

    private HttpStatus processUploading(Path file) {
        log.info("Start processing file '{}'", file);

        final HttpStatus httpStatus = restTemplate.postForEntity(
                        serverHost + "/api/upload-results-to-lab-order",
                        requestEntityProducer.uploadResultsToLabOrderRequestEntity(file, jwtInitializer.getJwt()),
                        Void.class
                )
                .getStatusCode();

        log.info("File '{} has been processed. Returned status: '{}'", file, httpStatus);

        return httpStatus;
    }

    private List<Path> files() {
        try {
            return Files.list(Paths.get(labInterfaceFilesPath))
                    .filter(path -> path.toFile().isFile())
                    .filter(path -> !path.toFile().isHidden())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("The exception has been occurred while reading directory", e);
            return Collections.emptyList();
        }
    }
}
