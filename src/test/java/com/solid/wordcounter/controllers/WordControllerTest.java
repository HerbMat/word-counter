package com.solid.wordcounter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solid.wordcounter.commands.WordCommand;
import com.solid.wordcounter.commands.WordCountResultCommand;
import com.solid.wordcounter.exception.GenericServiceException;
import com.solid.wordcounter.services.WordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class WordControllerTest {

    @MockBean
    private WordService wordService;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void addWord() throws Exception {
        WordCommand wordCommand = WordCommand.builder().word("word").build();
        mockMvc.perform(post("/word/add").content(toJson(wordCommand))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(wordService, times(1)).addWord(any(WordCommand.class));
    }

    @Test
    public void addWordFailed() throws Exception {
        WordCommand wordCommand = WordCommand.builder().word(" ").build();
        mockMvc.perform(post("/word/add").content(toJson(wordCommand))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("Word cannot be blank.")))
                .andExpect(content().string(containsString("Word can only have letters.")));

        verify(wordService, never()).addWord(any(WordCommand.class));
    }

    @Test
    public void getWordCount() throws Exception {
        WordCountResultCommand wordCountResultCommand = WordCountResultCommand.builder()
                .word("word")
                .counter(1)
                .build();

        when(wordService.getWordCount(anyString())).thenReturn(wordCountResultCommand);

        mockMvc.perform(get("/word/getCount/word"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.word", equalTo("word")))
                .andExpect(jsonPath("$.counter", equalTo(1)));
    }

    @Test
    public void getWordCountFailedSerivce() throws Exception {

        when(messageSource.getMessage(anyString(), eq(null), any(Locale.class)))
                .thenReturn("Error");
        when(wordService.getWordCount(anyString())).thenThrow(GenericServiceException.class);

        mockMvc.perform(get("/word/getCount/word"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0]", equalTo("Error")));
    }

    @Test
    public void getWordCountFailedValidation() throws Exception {

        mockMvc.perform(get("/word/getCount/2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("Word can only have letters.")));
    }

    private String toJson(Object r) throws Exception {
        return mapper.writeValueAsString(r);
    }
}
