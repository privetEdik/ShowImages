package kettlebell.showimages.validator;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ImageValidator {
    private static final Set<String> SUPPORTED_FORMATS = new HashSet<>(Arrays.asList(
            ImageIO.getReaderFormatNames()
    ));

    public boolean isValidImageExtension(String url) {
        String extension = getFileExtension(url);
        return SUPPORTED_FORMATS.contains(extension.toLowerCase());
    }

    private String getFileExtension(String url) {
        int lastDotIndex = url.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < url.length() - 1) {
            return url.substring(lastDotIndex + 1);
        }
        return ""; // no extension
    }
}
