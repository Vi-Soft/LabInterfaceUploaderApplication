package com.visoft.labinterfaceuploader.service;

import java.nio.file.Path;

public interface PathComposer {

    Path compose(Path file, String targetFolder);
}
