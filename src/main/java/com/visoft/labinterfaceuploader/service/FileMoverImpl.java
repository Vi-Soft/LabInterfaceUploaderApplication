package com.visoft.labinterfaceuploader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Log4j2
@Service
public class FileMoverImpl implements FileMover {

    @Value("${app.labInterfaceFilesPath}")
    private String labInterfaceFilesPath;

    @Value("${app.processedFiles}")
    private String processedFiles;

    @Value("${app.failedFiles}")
    private String failedFiles;

    private final PathComposer pathComposer;


    @Override
    public void prepareFolders() {
        createSubDirectory(processedFiles);
        createSubDirectory(failedFiles);
    }

    @Override
    public void move(HttpStatus httpStatus, Path file) {
        log.info("Start moving file: '{}'", file);

        final String targetFolder = httpStatus == HttpStatus.OK ? processedFiles : failedFiles;

        try {
            Files.move(file, pathComposer.compose(file, targetFolder));
        } catch (IOException e) {
            log.error("Error moving file: '{}'", file);
            return;
        }

        log.info("File '{}' moved to '{}' folder", file, targetFolder);
    }

    private void createSubDirectory(String subFolder) {
        final Path targetFolderPath = Paths.get(labInterfaceFilesPath, subFolder);

        if (!Files.exists(targetFolderPath)) {
            try {
                Files.createDirectory(targetFolderPath);

                log.info("Sub directory '{}' has been created", targetFolderPath);
            } catch (IOException e) {
                log.error("Error creating directory: '{}'", targetFolderPath);
            }
        }
    }
}
