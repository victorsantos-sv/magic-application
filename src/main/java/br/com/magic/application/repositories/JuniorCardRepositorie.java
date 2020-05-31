package br.com.magic.application.repositories;

import br.com.magic.application.entity.model.JuniorCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuniorCardRepositorie extends JpaRepository<JuniorCard, Long> {

    List<JuniorCard> findAllByPlayerId(Long playerId);

    List<JuniorCard> findAllByPlayerIsNullOrderById();

    Optional<JuniorCard> findByIdAndPlayerId(Long cardId, Long playerId);

}
