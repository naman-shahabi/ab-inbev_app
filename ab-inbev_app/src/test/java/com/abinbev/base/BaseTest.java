package com.abinbev.base;

import com.abinbev.config.i18n.InternationalizationConfiguration;
import com.abinbev.config.i18n.InternationalizationConfiguration.MessageSourceBinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Import(value = {
        InternationalizationConfiguration.class
})
@ActiveProfiles(value = {"test"})
@RunWith(SpringRunner.class)
public class BaseTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MessageSourceBinder messageSourceBinder;
}
