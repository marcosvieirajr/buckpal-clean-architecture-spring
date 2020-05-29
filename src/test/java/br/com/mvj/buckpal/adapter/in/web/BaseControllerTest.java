package br.com.mvj.buckpal.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class BaseControllerTest {

    @Autowired protected MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    protected String toJson(Object object) throws Exception {
        return this.mapper.writeValueAsString(object);
    }
}
