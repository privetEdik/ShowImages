package kettlebell.showimages.controller;

import jakarta.validation.Valid;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ImageDto addImage(@Valid @RequestBody ImageDto imageDto) {
        return imageService.addImage(imageDto);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
    }

    @GetMapping("/search")
    public List<ImageDto> searchImages(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) Integer duration) {
        if (keyword != null && !keyword.isBlank()) {
            return imageService.searchByKeyword(keyword);
        } else if (duration != null && duration > 0) {
            return imageService.searchByDuration(duration);
        } else return Collections.emptyList();

    }
}