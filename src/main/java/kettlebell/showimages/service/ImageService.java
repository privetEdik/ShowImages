package kettlebell.showimages.service;

import kettlebell.showimages.model.dto.ImageDto;

import java.util.List;

public interface ImageService {

    ImageDto addImage(ImageDto imageDto);

    void deleteImage(Long id);

    List<ImageDto> searchByDuration(Integer duration);

    List<ImageDto> searchByKeyword(String keyword);
}

