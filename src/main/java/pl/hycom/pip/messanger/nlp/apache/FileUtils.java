package pl.hycom.pip.messanger.nlp.apache;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Objects;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

@Log4j2
public class FileUtils {
    /**
     * Get file data as string
     *
     * @param fileName
     * @return
     */
    public static String getFileDataAsString(String fileName) {
        Objects.nonNull(fileName);
        try {
            return new String(readAllBytes(get(fileName)));
        } catch (IOException e) {
            log.error("Error during reading file" + e.getMessage());
            return null;
        }
    }
}