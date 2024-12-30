package kettlebell.showimages.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Builder
@Data
public class ImageDto {
    private Long id;
    private URL url;
    @NotNull(message = "Duration is required.")
    @Min(value = 1, message = "Duration must be greater than 0.")
    private Integer duration;
    private String date;
    private List<Long> slideShowIds;
}
