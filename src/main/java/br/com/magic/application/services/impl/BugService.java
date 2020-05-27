package br.com.magic.application.services.impl;

import br.com.magic.application.entity.dto.BugCardDTO;
import br.com.magic.application.entity.dto.BugDTO;
import br.com.magic.application.entity.mapper.BugMapper;
import br.com.magic.application.entity.model.Bug;
import br.com.magic.application.repositories.BugRepositorie;
import br.com.magic.application.services.IBugCardService;
import br.com.magic.application.services.IBugService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BugService implements IBugService {

    private BugRepositorie bugRepositorie;
    private IBugCardService bugCardService;
    private BugMapper bugMapper;

    @Autowired
    public BugService(BugRepositorie bugRepositorie, IBugCardService bugCardService, BugMapper bugMapper) {
        this.bugRepositorie = bugRepositorie;
        this.bugCardService = bugCardService;
        this.bugMapper = bugMapper;
    }

    @Override
    public BugDTO getInitialCards() {
        List<BugCardDTO> bugCardDTOList =  bugCardService.setCartsOnBug();
        Bug bug = bugRepositorie.save(new Bug());

        return bugMapper.toDto(bug, bugCardDTOList);
    }
}
