package br.com.magic.application.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ResourceBundle {

    private static MessageSource messageSource;

    private ResourceBundle(MessageSource messageSource) {
        ResourceBundle.messageSource = messageSource;
    }

    public String getMessage(String key, String... args) {
        if (messageSource != null) return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());

        return null;
    }
}
