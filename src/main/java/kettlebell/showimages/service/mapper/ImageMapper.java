package kettlebell.showimages.service.mapper;

import kettlebell.showimages.exception.UrlFormatException;
import kettlebell.showimages.model.Image;
import kettlebell.showimages.model.SlideShow;
import kettlebell.showimages.model.dto.ImageDto;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ImageMapper {

    public ImageDto toImageDto(Image image) {
        try {
            return ImageDto.builder()
                    .id(image.getId())
                    .url(new URL(image.getUrl()))
                    .duration(image.getDuration())
                    .date(image.getDate().toString())
                    .build();
        } catch (MalformedURLException e) {
            throw new UrlFormatException(e);
        }

    }

    public Image toImage(ImageDto imageDto) {
        Image image = new Image();
        image.setUrl(imageDto.getUrl().toString());
        image.setDuration(imageDto.getDuration());
        image.setDate(LocalDateTime.now());
        return image;
    }

    public List<ImageDto> toImageDtoListFromManySlideShow(List<Image> images) {
        return images.stream()
                .map(img -> {
                            try {
                                return ImageDto.builder()
                                        .id(img.getId())
                                        .url(new URL(img.getUrl()))
                                        .duration(img.getDuration())
                                        .date(img.getDate().toString())
                                        .slideShowIds(getSlideShowIds(img.getSlideshows()))
                                        .build();
                            } catch (MalformedURLException e) {
                                throw new UrlFormatException(e);
                            }
                        }
                )
                .toList();
    }

    private List<Long> getSlideShowIds(List<SlideShow> list) {
        return list.stream().map(SlideShow::getId).toList();
    }

    public List<ImageDto> toImageDtoListFromOneSlideShow(List<Image> images, Long slideShowId) {

        List<Long> listSlideShowId = List.of(slideShowId);
        return images.stream()
                .map(img -> {
                    try {
                        return ImageDto.builder()
                                .id(img.getId())
                                .url(new URL(img.getUrl()))
                                .duration(img.getDuration())
                                .date(img.getDate().toString())
                                .slideShowIds(listSlideShowId)
                                .build();
                    } catch (MalformedURLException e) {
                        throw new UrlFormatException(e);
                    }
                })
                .toList();
    }
}
