package kettlebell.showimages.service;

import jakarta.persistence.EntityNotFoundException;
import kettlebell.showimages.event.ProofOfPlayEvent;
import kettlebell.showimages.model.Image;
import kettlebell.showimages.model.ProofOfPlay;
import kettlebell.showimages.model.SlideShow;
import kettlebell.showimages.model.dto.ImageDto;
import kettlebell.showimages.repository.ImageRepository;
import kettlebell.showimages.repository.ProofOfPlayRepository;
import kettlebell.showimages.repository.SlideShowRepository;
import kettlebell.showimages.service.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class SlideShowServiceImpl implements SlideShowService {

    private final SlideShowRepository slideShowRepository;
    private final ImageRepository imageRepository;
    private final ProofOfPlayRepository proofOfPlayRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageMapper imageMapper;

    @Override
    public Long createSlideShow(List<ImageDto> imageDtoList) {

        List<String> urls = imageDtoList.stream()
                .map(dto -> dto.getUrl().toString())
                .toList();

        List<Image> images = imageRepository.findByUrlIn(urls);

        if (images.isEmpty()) {
            throw new EntityNotFoundException("image urls for slideshow not found in database");
        }

        SlideShow slideShow = new SlideShow();
        slideShow.setImages(images);

        return slideShowRepository.save(slideShow).getId();
    }

    @Override
    @Transactional
    public void deleteSlideShow(Long slideShowId) {
        if (slideShowRepository.existsById(slideShowId)) {
            slideShowRepository.deleteById(slideShowId);
        } else throw new EntityNotFoundException("SlideShow doesn't exist");
    }

    @Override
    public List<ImageDto> getImagesInOrder(Long slideShowId) {
        List<Image> images = slideShowRepository.findImagesBySlideShowIdOrderByDate(slideShowId);

        if (images.isEmpty()) {
            throw new EntityNotFoundException("SlideShow not found with ID: " + slideShowId);
        }
        return imageMapper.toImageDtoListFromOneSlideShow(images, slideShowId);
    }

    @Override
    public void saveProofOfPlayAsync(Long slideShowId, Long imageId) {

        if (!slideShowRepository.existsById(slideShowId)) {
            throw new EntityNotFoundException("SlideShow not found with ID: " + slideShowId);
        }
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with ID: " + imageId));

        ProofOfPlay proofOfPlay = new ProofOfPlay();
        proofOfPlay.setSlideshowId(slideShowId);
        proofOfPlay.setImageId(imageId);
        proofOfPlay.setTimestamp(LocalDateTime.now());

        final Supplier<?> action = () -> proofOfPlayRepository.save(proofOfPlay);
        eventPublisher.publishEvent(new ProofOfPlayEvent(this, image.getUrl(), slideShowId));

        CompletableFuture.completedFuture(action);

    }
}

