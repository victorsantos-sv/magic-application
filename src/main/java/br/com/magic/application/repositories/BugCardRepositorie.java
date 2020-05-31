package br.com.magic.application.repositories;

import br.com.magic.application.entity.model.BugCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugCardRepositorie extends JpaRepository<BugCard, Long> {

    List<BugCard> findAllByBugIsNull();

    List<BugCard> findAllByBugId(Long bugId);
}
