package br.com.magic.application.api.response;

import java.util.List;

public class StackCardsResponse {
    private List<JuniorCardResponse> juniorCards;
    private List<BugCardResponse> bugCards;

    public StackCardsResponse() {
    }

    public StackCardsResponse(List<JuniorCardResponse> juniorCards, List<BugCardResponse> bugCards) {
        this.juniorCards = juniorCards;
        this.bugCards = bugCards;
    }

    public List<JuniorCardResponse> getJuniorCards() {
        return juniorCards;
    }

    public void setJuniorCards(List<JuniorCardResponse> juniorCards) {
        this.juniorCards = juniorCards;
    }

    public List<BugCardResponse> getBugCards() {
        return bugCards;
    }

    public void setBugCards(List<BugCardResponse> bugCards) {
        this.bugCards = bugCards;
    }
}
