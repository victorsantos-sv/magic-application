package br.com.magic.application.services.impl;

import br.com.magic.application.entity.dto.*;
import br.com.magic.application.entity.mapper.JuniorCardMapper;
import br.com.magic.application.entity.mapper.PlayerMapper;
import br.com.magic.application.services.IJuniorCardService;
import br.com.magic.application.services.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;

public class ScoreboardPlayerService {

    private IPlayerService playerService;
    private IJuniorCardService juniorCardService;
    private PlayerDTO player;
    private JuniorCardMapper mapper;

    @Autowired
    public ScoreboardPlayerService(IPlayerService playerService, IJuniorCardService juniorCardService, PlayerDTO player, JuniorCardMapper mapper) {
        this.playerService = playerService;
        this.juniorCardService = juniorCardService;
        this.player = player;
        this.mapper = mapper;
    }

    @Override
    public ScoreboardPlayerDTO scoreboard(Long id, Long idCard) {
        PlayerDTO playerDTO = playerService.findById(id);
        JuniorCardDTO cardID = juniorCardService.findById(idCard);
        JuniorCardDTO mana = mana(cardID);
        JuniorCardDTO life = life(cardID);
        playerDTO.setMana(manaAmount);
        playerDTO.setLife(lifeAmount);
        return mapper.dto();
    }

    Integer manaAmount;
    Integer lifeAmount;

    public JuniorCardDTO mana (JuniorCardDTO card){
        manaAmount = card.getPassive() == null ?
                player.getMana() - card.getCost(): player.getMana() +card.getPassive();
        return card;
    }

    public JuniorCardDTO life (JuniorCardDTO card){

        lifeAmount = card.getPassive() == null ?
                player.getLife() - card.getLifeDamage() : player.getMana() +card.getPassive();
        return card;
    }


}
