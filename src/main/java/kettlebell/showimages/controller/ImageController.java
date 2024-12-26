package kettlebell.showimages.controller;

import jakarta.validation.Valid;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.service.ImageService;
import kettlebell.showimages.service.SlideShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class ImageController {

    private final ImageService imageService;
    private final SlideShowService slideShowService;

    @PostMapping("/addImage")
    public ResponseEntity<ImageDto> addImage(@Valid @RequestBody ImageDto imageDto) {
        return ResponseEntity.ok(imageService.addImage(imageDto));
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addSlideshow")
    public ResponseEntity<?> addSlideshow(@Valid @RequestBody List<ImageDto> imageDtos) {
        return ResponseEntity.ok(slideShowService.createSlideShow(imageDtos));
    }

    @DeleteMapping("/deleteSlideshow/{id}")
    public ResponseEntity<?> deleteSlideshow(@PathVariable Long id) {
        slideShowService.deleteSlideShow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/images/search")
    public ResponseEntity<?> searchImages(@RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) Integer duration) {
        return ResponseEntity.ok(imageService.searchImages(keyword, duration));
    }

    @GetMapping("/slideShow/{id}/slideshowOrder")
    public ResponseEntity<?> getSlideshowOrder(@PathVariable Long id) {
        return ResponseEntity.ok(slideShowService.getImagesInOrder(id));
    }

    @PostMapping("/slideShow/{id}/proof-of-play/{imageId}")
    public void recordProofOfPlay(@PathVariable Long id, @PathVariable Long imageId) {
        slideShowService.saveProofOfPlayAsync(id, imageId);
    }

}

