package kettlebell.showimages.exception;

public class ImageExistsException extends RuntimeException {
    public ImageExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
