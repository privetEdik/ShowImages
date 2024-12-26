package kettlebell.showimages.repository;

import kettlebell.showimages.model.Image;
import kettlebell.showimages.model.SlideShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideShowRepository extends JpaRepository<SlideShow, Long> {

    @Query("SELECT i FROM SlideShow s JOIN s.images i " +
            "WHERE s.id = :slideshowId " +
            "ORDER BY i.date ASC")
    List<Image> findImagesBySlideShowIdOrderByDate(@Param("slideshowId") Long slideshowId);
}
