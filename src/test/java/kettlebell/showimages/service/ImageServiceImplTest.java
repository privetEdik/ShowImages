package kettlebell.showimages.service;

import jakarta.persistence.EntityNotFoundException;
import kettlebell.showimages.event.ImageAddedEvent;
import kettlebell.showimages.event.ImageDeletedEvent;
import kettlebell.showimages.model.Image;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.repository.ImageRepository;
import kettlebell.showimages.validator.ImageValidator;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ImageServiceImplTest {

    private final ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
    private final ApplicationEventPublisher eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
    private final ImageValidator validator = Mockito.mock(ImageValidator.class);
    private final ImageServiceImpl imageService = new ImageServiceImpl(imageRepository, eventPublisher, validator);

    @Test
    void addImage_ShouldSaveImage_WhenValidDataProvided() {
        // Arrange
        ImageDto imageDto = ImageDto.builder()
                .url("http://example.com/image.jpg")
                .duration(10)
                .build();

        Image savedImage = new Image();
        savedImage.setId(1L);
        savedImage.setUrl(imageDto.getUrl());
        savedImage.setDuration(imageDto.getDuration());
        savedImage.setDate(LocalDateTime.now());

        Mockito.when(validator.isValidImageExtension(imageDto.getUrl())).thenReturn(true);
        Mockito.when(imageRepository.save(Mockito.any(Image.class))).thenReturn(savedImage);

        // Act
        ImageDto result = imageService.addImage(imageDto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedImage.getId(), result.getId());
        Mockito.verify(eventPublisher).publishEvent(Mockito.any(ImageAddedEvent.class));
    }

    @Test
    void addImage_ShouldThrowException_WhenUrlIsInvalid() {
        // Arrange
        ImageDto imageDto = ImageDto.builder().url("invalid_url").build();

        Mockito.when(validator.isValidImageExtension(imageDto.getUrl())).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> imageService.addImage(imageDto));
        Mockito.verifyNoInteractions(imageRepository);
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void deleteImage_ShouldDeleteImage_WhenImageExists() {
        // Arrange
        Long imageId = 1L;
        Mockito.when(imageRepository.existsById(imageId)).thenReturn(true);

        // Act
        imageService.deleteImage(imageId);

        // Assert
        Mockito.verify(imageRepository).deleteById(imageId);
        Mockito.verify(eventPublisher).publishEvent(Mockito.any(ImageDeletedEvent.class));
    }

    @Test
    void deleteImage_ShouldThrowException_WhenImageNotFound() {
        // Arrange
        Long imageId = 1L;
        Mockito.when(imageRepository.existsById(imageId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> imageService.deleteImage(imageId));
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void searchImages_ShouldReturnMappedDto_WhenImagesFound() {
        // Arrange
        String keyword = "example";
        Integer duration = 10;

        Image image1 = new Image();
        image1.setId(1L);
        image1.setUrl("http://example.com/image1.jpg");
        image1.setDuration(duration);
        image1.setDate(LocalDateTime.now());

        Image image2 = new Image();
        image2.setId(2L);
        image2.setUrl("http://example.com/image2.jpg");
        image2.setDuration(duration);
        image2.setDate(LocalDateTime.now());

        Mockito.when(imageRepository.findImagesByKeyword(keyword)).thenReturn(List.of(image1));
        Mockito.when(imageRepository.findImagesByDuration(duration)).thenReturn(List.of(image2));

        // Act
        List<ImageDto> result = imageService.searchImages(keyword, duration);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(image1.getId())));
        Assertions.assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(image2.getId())));
    }

    @Test
    void searchImages_ShouldReturnMappedDto_WhenImagesFound_WhenKeywordEmpty() {
        // Arrange
        String keyword = "";
        Integer duration = 10;

        Image image1 = new Image();
        image1.setId(1L);
        image1.setUrl("http://example.com/image1.jpg");
        image1.setDuration(duration);
        image1.setDate(LocalDateTime.now());

        Image image2 = new Image();
        image2.setId(2L);
        image2.setUrl("http://example.com/image2.jpg");
        image2.setDuration(duration);
        image2.setDate(LocalDateTime.now());

        Mockito.when(imageRepository.findImagesByKeyword(keyword)).thenReturn(new ArrayList<>());
        Mockito.when(imageRepository.findImagesByDuration(duration)).thenReturn(List.of(image2));

        // Act
        List<ImageDto> result = imageService.searchImages(keyword, duration);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertFalse(result.stream().anyMatch(dto -> dto.getId().equals(image1.getId())));
        Assertions.assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(image2.getId())));
    }

    @Test
    void searchImages_ShouldReturnMappedDto_WhenImagesFound_WhenDurationInvalid() {
        // Arrange
        String keyword = "example";
        Integer duration = -1;

        Image image1 = new Image();
        image1.setId(1L);
        image1.setUrl("http://example.com/image1.jpg");
        image1.setDuration(duration);
        image1.setDate(LocalDateTime.now());

        Image image2 = new Image();
        image2.setId(2L);
        image2.setUrl("http://example.com/image2.jpg");
        image2.setDuration(duration);
        image2.setDate(LocalDateTime.now());

        Mockito.when(imageRepository.findImagesByKeyword(keyword)).thenReturn(List.of(image1));
        Mockito.when(imageRepository.findImagesByDuration(duration)).thenReturn(new ArrayList<>());

        // Act
        List<ImageDto> result = imageService.searchImages(keyword, duration);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(image1.getId())));
        Assertions.assertFalse(result.stream().anyMatch(dto -> dto.getId().equals(image2.getId())));
    }
}