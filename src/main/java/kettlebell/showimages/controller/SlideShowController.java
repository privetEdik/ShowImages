package kettlebell.showimages.controller;

import jakarta.validation.Valid;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.service.SlideShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slideshow")
@RequiredArgsConstructor
public class SlideShowController {

    private final SlideShowService slideShowService;

    @PostMapping
    public Long addSlideshow(@Valid @RequestBody List<ImageDto> imageDtoList) {
        return slideShowService.createSlideShow(imageDtoList);
    }

    @DeleteMapping("/{id}")
    public void deleteSlideshow(@PathVariable Long id) {
        slideShowService.deleteSlideShow(id);
    }

    @GetMapping("/{id}/order")
    public List<ImageDto> getSlideshowWithOrderForImages(@PathVariable Long id) {
        return slideShowService.getImagesInOrder(id);
    }

    @PostMapping("/{id}/proof-of-play/{imageId}")
    public void recordProofOfPlay(@PathVariable Long id, @PathVariable Long imageId) {
        slideShowService.saveProofOfPlayAsync(id, imageId);
    }
}
