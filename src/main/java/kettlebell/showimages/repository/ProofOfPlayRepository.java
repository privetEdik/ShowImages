package kettlebell.showimages.repository;

import kettlebell.showimages.model.ProofOfPlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProofOfPlayRepository extends JpaRepository<ProofOfPlay, Long> {
}