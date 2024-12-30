package kettlebell.showimages.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToMany(mappedBy = "images", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SlideShow> slideshows = new ArrayList<>();
}
