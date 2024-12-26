package kettlebell.showimages.exception;

public class ImageExistsException extends RuntimeException{
    public ImageExistsException(String message) {
        super(message);
    }
}
