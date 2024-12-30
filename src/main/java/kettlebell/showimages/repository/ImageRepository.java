package kettlebell.showimages.repository;

import kettlebell.showimages.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUrlIn(List<String> urls);

    @Query("SELECT i FROM Image i LEFT JOIN i.slideshows s WHERE i.url LIKE %:keyword%")
    List<Image> findImagesByKeyword(@Param("keyword") String keyword);

    @Query("SELECT i FROM Image i LEFT JOIN i.slideshows s WHERE i.duration = :duration")
    List<Image> findImagesByDuration(@Param("duration") Integer duration);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Image i WHERE i.id = :id) THEN true ELSE false END")
    boolean existsById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM slideshow_images WHERE image_id = :imageId", nativeQuery = true)
    void removeImageFromAllSlideShows(@Param("imageId") Long imageId);

    @Modifying
    @Query(value = "DELETE FROM image WHERE id = :imageId", nativeQuery = true)
    void deleteImageById(@Param("imageId") Long imageId);

}
