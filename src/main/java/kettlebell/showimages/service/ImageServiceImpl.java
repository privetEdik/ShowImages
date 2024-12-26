package kettlebell.showimages.service;

import jakarta.persistence.EntityNotFoundException;
import kettlebell.showimages.exception.ImageExistsException;
import kettlebell.showimages.event.ImageAddedEvent;
import kettlebell.showimages.event.ImageDeletedEvent;
import kettlebell.showimages.model.Image;
import kettlebell.showimages.model.SlideShow;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.repository.ImageRepository;
import kettlebell.showimages.validator.ImageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final ImageRepository imageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageValidator validator;

    public ImageDto addImage(ImageDto imageDto) {

        if (!validator.isValidImageExtension(imageDto.getUrl())) {
            throw new IllegalArgumentException("Invalid image URL");
        }
        Image image = new Image();
        image.setUrl(imageDto.getUrl());
        image.setDuration(imageDto.getDuration());
        image.setDate(LocalDateTime.now());
        try {
            Image savedImage = imageRepository.save(image);

            eventPublisher.publishEvent(new ImageAddedEvent(this, savedImage.getId()));

            return ImageDto.builder()
                    .id(savedImage.getId())
                    .url(savedImage.getUrl())
                    .duration(savedImage.getDuration())
                    .date(savedImage.getDate().toString())
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new ImageExistsException("image with this address already exists");
        }
    }

    public void deleteImage(Long id) {

        if (!imageRepository.existsById(id)) {
            throw new EntityNotFoundException("Image not found");
        }
        imageRepository.deleteById(id);

        eventPublisher.publishEvent(new ImageDeletedEvent(this, id));
    }

    public List<ImageDto> searchImages(String keyword, Integer duration) {
        List<Image> keywordResults = keyword != null && !keyword.isBlank()
                ? imageRepository.findImagesByKeyword(keyword)
                : new ArrayList<>();

        List<Image> durationResults = duration != null && duration > 0
                ? imageRepository.findImagesByDuration(duration)
                : new ArrayList<>();

        List<Image> images = Stream.concat(keywordResults.stream(), durationResults.stream())
                .distinct()
                .toList();

        return mappingToDto(images);
    }

    private List<ImageDto> mappingToDto(List<Image> images) {
        return images.stream()
                .map(img -> ImageDto.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .duration(img.getDuration())
                        .date(img.getDate().toString())
                        .slideShowIds(getSlideShowIds(img.getSlideshows()))
                        .build())
                .toList();
    }

    private List<Long> getSlideShowIds(List<SlideShow> list) {
        return list.stream().map(SlideShow::getId).toList();
    }

}
