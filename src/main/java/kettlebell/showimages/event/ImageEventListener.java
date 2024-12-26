package kettlebell.showimages.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImageEventListener {

    @EventListener
    public void handleImageAddedEvent(ImageAddedEvent event) {
        log.info("Image with ID {} was added.", event.getImageId());
    }

    @EventListener
    public void handleImageDeletedEvent(ImageDeletedEvent event) {
        log.info("Image with ID {} was deleted.", event.getImageId());
    }

    @EventListener
    public void handleProofOfPlayEvent(ProofOfPlayEvent event) {
        log.info("SlideShow with ID {} shows image with url {}",
                event.getSlideShowId(), event.getUrlImage());
    }
}
