package kettlebell.showimages.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ImageDeletedEvent extends ApplicationEvent {
    private final Long imageId;

    public ImageDeletedEvent(Object source, Long imageId) {
        super(source);
        this.imageId = imageId;
    }
}
