package br.com.magic.application.api.request;

import javax.validation.constraints.NotEmpty;

public class PlayerRequest {
    @NotEmpty
    private String nickName;

    public PlayerRequest() {
    }

    public PlayerRequest(@NotEmpty String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
