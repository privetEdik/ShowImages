package kettlebell.showimages.service;

import jakarta.persistence.EntityNotFoundException;
import kettlebell.showimages.exception.ImageExistsException;
import kettlebell.showimages.event.ImageAddedEvent;
import kettlebell.showimages.event.ImageDeletedEvent;
import kettlebell.showimages.model.Image;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.repository.ImageRepository;
import kettlebell.showimages.service.mapper.ImageMapper;
import kettlebell.showimages.service.validator.ImageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageValidator validator;
    private final ImageMapper imageMapper;


    public ImageDto addImage(ImageDto imageDto) {

        if (validator.isNotValidImageExtension(imageDto.getUrl().toString())) {
            throw new IllegalArgumentException("image url does not contain a valid extension");
        }

        Image image = imageMapper.toImage(imageDto);

        try {
            Image savedImage = imageRepository.save(image);
            eventPublisher.publishEvent(new ImageAddedEvent(this, savedImage.getId()));
            return imageMapper.toImageDto(savedImage);
        } catch (DataIntegrityViolationException e) {
            throw new ImageExistsException("image with this address already exists", e);
        }
    }

    @Transactional
    public void deleteImage(Long id) {
        if (imageRepository.existsById(id)) {
            imageRepository.removeImageFromAllSlideShows(id);
            imageRepository.deleteImageById(id);
            eventPublisher.publishEvent(new ImageDeletedEvent(this, id));
        } else throw new EntityNotFoundException("Image doesn't exist");
    }

    public List<ImageDto> searchByKeyword(String keyword) {
        List<Image> keywordResults = imageRepository.findImagesByKeyword(keyword);
        return imageMapper.toImageDtoListFromManySlideShow(keywordResults);
    }

    public List<ImageDto> searchByDuration(Integer duration) {
        List<Image> durationResults = imageRepository.findImagesByDuration(duration);
        return imageMapper.toImageDtoListFromManySlideShow(durationResults);
    }

}
