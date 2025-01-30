package org.polibot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class EnviarRes {

    private SendMessage message;

    public EnviarRes (long chatId, String text) {
        message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    public SendMessage getMessage() {
        return message;
    }

    public void setMessage(SendMessage message) {
        this.message = message;
    }
}
