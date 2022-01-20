package com.visoft.labinterfaceuploader.service;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PathComposerImpl implements PathComposer {

    @Override
    public Path compose(Path file, String targetFolder) {
        final Path path = composeTargetPath(file, targetFolder);

        return Paths.get(path.getParent().toString(), chooseName(path));
    }

    private Path composeTargetPath(Path file, String targetFolder) {
        return Paths.get(
                file.getParent().toString(),
                targetFolder,
                file.getFileName().toString()
        );
    }

    static String chooseName(Path path) {
        if (isFileExists(path)) {
            return generateUniqueName(path.getParent(), path.getFileName().toString(), 1);
        }
        return path.getFileName().toString();
    }

    static String generateUniqueName(Path parent, String filename, int counter) {
        final Path newPath = Paths.get(parent.toString(), composeName(filename, counter));

        if (isFileExists(newPath)) {
            return generateUniqueName(parent, filename, ++counter);
        }

        return newPath.getFileName().toString();
    }

    static String composeName(String filename, int counter) {
        final int indexOfDot = filename.indexOf(".");

        if (indexOfDot <= 0) {
            return filename + " (" + counter + ")";
        }

        return filename.substring(0, indexOfDot) + " (" + counter + ")." + filename.substring(indexOfDot + 1);
    }

    static boolean isFileExists(Path path) {
        return path.toFile().exists();
    }
}
