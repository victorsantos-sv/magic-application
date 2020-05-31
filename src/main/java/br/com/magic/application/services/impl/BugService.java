package br.com.magic.application.services.impl;

import br.com.magic.application.commons.MagicErrorCode;
import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.dto.BugWithCardsDTO;
import br.com.magic.application.entity.mapper.BugMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.exception.BugNotFound;
import br.com.magic.application.repositories.BugRepositorie;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.services.IBugService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BugService implements IBugService {

    private final BugRepositorie bugRepositorie;
    private final IBugCardService bugCardService;
    private final BugMapper bugMapper;

    public BugService(BugRepositorie bugRepositorie, IBugCardService bugCardService, BugMapper bugMapper) {
        this.bugRepositorie = bugRepositorie;
        this.bugCardService = bugCardService;
        this.bugMapper = bugMapper;
    }

    @Override
    public BugDTO createBug() {
        Bug bug = new Bug();

        return bugMapper.toDto(bugRepositorie.save(bug));
    }

    @Override
    public BugWithCardsDTO getInitialCards(Long bugId) {
        BugDTO bugDTO = findById(bugId);

        return bugCardService.setCardsOnBug(bugDTO);
    }

    @Override
    public BugDTO findById(Long id) {
        Bug bug = bugRepositorie.findById(id).orElseThrow(() -> new BugNotFound(MagicErrorCode.MEC005));

        return bugMapper.toDto(bug);
    }

    @Override
    public BugDTO updateBug(BugDTO bugDTO) {
        Bug bug = bugMapper.toEntity(bugDTO);

        return bugMapper.toDto(bugRepositorie.save(bug));
    }

    @Override
    public void deleteAllBugs() {
        bugRepositorie.deleteAll();
    }
}
