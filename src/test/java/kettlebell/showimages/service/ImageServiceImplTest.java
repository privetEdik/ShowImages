package kettlebell.showimages.service;

import jakarta.persistence.EntityNotFoundException;
import kettlebell.showimages.event.ImageAddedEvent;
import kettlebell.showimages.event.ImageDeletedEvent;
import kettlebell.showimages.model.Image;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.repository.ImageRepository;
import kettlebell.showimages.service.mapper.ImageMapper;
import kettlebell.showimages.service.validator.ImageValidator;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

class ImageServiceImplTest {

    private final ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
    private final ApplicationEventPublisher eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
    private final ImageValidator validator = Mockito.mock(ImageValidator.class);
    private final ImageMapper imageMapper = Mockito.mock(ImageMapper.class);
    private final ImageServiceImpl imageService = new ImageServiceImpl(imageRepository, eventPublisher, validator, imageMapper);

    @Test
    void addImage_ShouldSaveImage_WhenValidDataProvided() {
        // Arrange
        URL url;
        String urlString = "https://example.com/image.jpg";
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ImageDto imageDto = ImageDto.builder()
                .id(1L)
                .url(url)
                .duration(10)
                .build();

        Image savedImage = new Image();
        savedImage.setId(1L);
        savedImage.setUrl(urlString);
        savedImage.setDuration(imageDto.getDuration());
        savedImage.setDate(LocalDateTime.now());

        Mockito.when(validator.isNotValidImageExtension(Mockito.anyString())).thenReturn(false);
        Mockito.when(imageMapper.toImage(imageDto)).thenReturn(savedImage);
        Mockito.when(imageRepository.save(Mockito.any(Image.class))).thenReturn(savedImage);
        Mockito.when(imageMapper.toImageDto(Mockito.any(Image.class))).thenReturn(imageDto);

        // Act
        ImageDto result = imageService.addImage(imageDto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedImage.getId(), result.getId());
        Mockito.verify(imageMapper).toImage(Mockito.any(ImageDto.class));
        Mockito.verify(imageRepository).save(Mockito.any(Image.class));
        Mockito.verify(imageMapper).toImageDto(Mockito.any(Image.class));
        Mockito.verify(eventPublisher).publishEvent(Mockito.any(ImageAddedEvent.class));
    }

    @Test
    void addImage_ShouldThrowException_WhenUrlIsInvalid() {
        // Arrange
        URL url;
        String urlString = "https://example.com/image.jpg";
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ImageDto imageDto = ImageDto.builder().url(url).build();

        Mockito.when(validator.isNotValidImageExtension(Mockito.anyString())).thenReturn(true);

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
        Mockito.verify(imageRepository).deleteImageById(imageId);
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

}