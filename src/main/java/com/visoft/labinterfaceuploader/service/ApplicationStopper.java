package com.visoft.labinterfaceuploader.service;

public interface ApplicationStopper {

    /**
     * Properly stop application if condition is true
     */
    void checkStopAfterFirstJobCondition();

    /**
     * Properly stop application
     */
    void stop(int exitCode);
}
