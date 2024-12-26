package kettlebell.showimages.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ImageDto {
    private Long id;
    @NotBlank(message = "URL is required.")
    @Size(max = 2048, message = "URL must not exceed 2048 characters.")
    private String url;
    @NotNull(message = "Duration is required.")
    @Min(value = 1, message = "Duration must be greater than 0.")
    private Integer duration;
    private String date;
    private List<Long> slideShowIds;

}
