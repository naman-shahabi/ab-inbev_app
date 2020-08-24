package com.abinbev.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
public class BaseMockMvcTest extends BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
}
