package com.chahel.ch0623.controller;

import com.chahel.ch0623.service.ToolRentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ToolRentalController.class)
public class ToolRentalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToolRentalService toolRentalServ;

    @Test
    public void requestingGetOnNonexistentIdShouldReturn4xxResponse() throws Exception {
        when(toolRentalServ.findById(0)).thenReturn(null);
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().is4xxClientError());
    }
}
