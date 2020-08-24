package com.abinbev.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

@Configuration
public class InternationalizationConfiguration {

    @Bean
    public MessageSource messageSource(){
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    public MessageSourceBinder messageSourceBinder(MessageSource messageSource){
        return new MessageSourceBinder(messageSource);
    }

    public static class MessageSourceBinder {
        private final MessageSource messageSource;

        public MessageSourceBinder(MessageSource messageSource) {
            this.messageSource = messageSource;
        }

        public String getMessage(final String path, String ... arguments){
            return this.messageSource.getMessage(path, arguments, Locale.getDefault());
        }
    }

}
