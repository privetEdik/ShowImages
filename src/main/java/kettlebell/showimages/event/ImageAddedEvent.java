package kettlebell.showimages.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ImageAddedEvent extends ApplicationEvent {
    private final Long imageId;

    public ImageAddedEvent(Object source, Long imageId) {
        super(source);
        this.imageId = imageId;
    }

}
