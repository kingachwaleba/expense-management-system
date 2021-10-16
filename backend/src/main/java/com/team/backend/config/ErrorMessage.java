package com.team.backend.config;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class ErrorMessage {

    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    public ErrorMessage(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, new Locale("pl"));
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }
}
