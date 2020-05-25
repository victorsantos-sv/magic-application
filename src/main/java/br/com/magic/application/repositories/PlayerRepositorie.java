package br.com.magic.application.repositories;

import br.com.magic.application.entity.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepositorie extends JpaRepository<Player, Long> {
}
