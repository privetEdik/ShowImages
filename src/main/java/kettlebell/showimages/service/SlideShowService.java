package kettlebell.showimages.service;

import kettlebell.showimages.model.dto.ImageDto;

import java.util.List;

public interface SlideShowService {
    Long createSlideShow(List<ImageDto> imageDtos);

    void deleteSlideShow(Long slideShowId);

    List<ImageDto> getImagesInOrder(Long slideShowId);

    void saveProofOfPlayAsync(Long slideShowId, Long imageId);
}
