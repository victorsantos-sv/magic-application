package br.com.magic.application.entity.dto;

import java.util.List;

public class StackCardsDTO {
    private List<JuniorCardDTO> juniorCards;
    private List<BugCardDTO> bugCards;

    public StackCardsDTO() {
    }

    public StackCardsDTO(List<JuniorCardDTO> juniorCards, List<BugCardDTO> bugCards) {
        this.juniorCards = juniorCards;
        this.bugCards = bugCards;
    }

    public List<JuniorCardDTO> getJuniorCards() {
        return juniorCards;
    }

    public void setJuniorCards(List<JuniorCardDTO> juniorCards) {
        this.juniorCards = juniorCards;
    }

    public List<BugCardDTO> getBugCards() {
        return bugCards;
    }

    public void setBugCards(List<BugCardDTO> bugCards) {
        this.bugCards = bugCards;
    }
}
