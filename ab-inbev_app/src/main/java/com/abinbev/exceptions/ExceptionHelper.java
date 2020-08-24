package com.abinbev.exceptions;

import com.abinbev.config.i18n.InternationalizationConfiguration.MessageSourceBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@Component
public class ExceptionHelper {

    private final MessageSourceBinder messageSourceBinder;

    public ExceptionHelper(MessageSourceBinder messageSourceBinder) {
        this.messageSourceBinder = messageSourceBinder;
    }

    public <T extends ServiceException> void throwServiceException(final Class<T> clazz, final String messagePath, final String... arguments) {
        throw this.serviceException(clazz, messagePath, arguments);
    }

    public <T extends ServiceException> T serviceException(final Class<T> clazz, final String messagePath, final String... arguments) {
        try {
            final Constructor<T> constructor = clazz.getConstructor(String.class);
            final String exceptionMessage = this.messageSourceBinder.getMessage(messagePath, arguments);
            return constructor.newInstance(exceptionMessage);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Exception when trying to exception instance", e);
        }
        throw new RuntimeException(String.format("Failed to create service from class: %s", clazz.getCanonicalName()));
    }
}
