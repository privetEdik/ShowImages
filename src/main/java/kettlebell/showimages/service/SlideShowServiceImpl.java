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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

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

    @Override
    public Long createSlideShow(List<ImageDto> imageDtoList) {

        List<String> urls = imageDtoList.stream()
                .map(ImageDto::getUrl)
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
    public void deleteSlideShow(Long slideShowId) {
        slideShowRepository.findById(slideShowId)
                .orElseThrow(() -> new EntityNotFoundException("SlideShow not found"));
        slideShowRepository.deleteById(slideShowId);

    }

    @Override
    public List<ImageDto> getImagesInOrder(Long slideShowId) {
        List<Image> images = slideShowRepository.findImagesBySlideShowIdOrderByDate(slideShowId);

        if (images.isEmpty()) {
            throw new EntityNotFoundException("SlideShow not found with ID: " + slideShowId);
        }
        return mappingToDto(images, slideShowId);
    }

    @Override
    public void saveProofOfPlayAsync(Long slideShowId, Long imageId) {
        slideShowRepository.findById(slideShowId)
                .orElseThrow(() -> new EntityNotFoundException("SlideShow not found with ID: " + slideShowId));

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

    private List<ImageDto> mappingToDto(List<Image> images, Long slideShowId) {
        List<Long> listSlideShowId = List.of(slideShowId);
        return images.stream()
                .map(img -> ImageDto.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .duration(img.getDuration())
                        .date(img.getDate().toString())
                        .slideShowIds(listSlideShowId)
                        .build())
                .toList();
    }
}

