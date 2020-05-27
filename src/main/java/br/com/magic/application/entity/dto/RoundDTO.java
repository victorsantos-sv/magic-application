package br.com.magic.application.entity.dto;

public class RoundDTO {
    private PlayerDTO playerDTO;
    private BugDTO bugDTO;
    private Long cardId;

    public RoundDTO() {
    }

    public RoundDTO(PlayerDTO playerDTO, BugDTO bugDTO, Long cardId) {
        this.playerDTO = playerDTO;
        this.bugDTO = bugDTO;
        this.cardId = cardId;
    }

    public PlayerDTO getPlayerDTO() {
        return playerDTO;
    }

    public void setPlayerDTO(PlayerDTO playerDTO) {
        this.playerDTO = playerDTO;
    }

    public BugDTO getBugDTO() {
        return bugDTO;
    }

    public void setBugDTO(BugDTO bugDTO) {
        this.bugDTO = bugDTO;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
