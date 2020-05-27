package br.com.magic.application.repositories;

import br.com.magic.application.entity.model.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugRepositorie extends JpaRepository<Bug, Long> {
}
