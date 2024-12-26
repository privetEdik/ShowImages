package kettlebell.showimages.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProofOfPlayEvent extends ApplicationEvent {
    private final String urlImage;
    private final Long slideShowId;

    public ProofOfPlayEvent(Object source, String urlImage, Long slideShowId) {
        super(source);
        this.urlImage = urlImage;
        this.slideShowId = slideShowId;
    }
}
