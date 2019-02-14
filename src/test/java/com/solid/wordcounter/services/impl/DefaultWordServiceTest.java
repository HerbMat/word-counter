package com.solid.wordcounter.services.impl;

import com.solid.wordcounter.commands.WordCommand;
import com.solid.wordcounter.commands.WordCountResultCommand;
import com.solid.wordcounter.domain.Word;
import com.solid.wordcounter.exception.GenericServiceException;
import com.solid.wordcounter.repositories.WordRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DefaultWordServiceTest {

    @Mock
    private WordRepository wordRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DefaultWordService defaultWordService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getWordCountExisting() {
        Word wordMock = new Word();
        wordMock.setCounter(3L);
        wordMock.setWord("existing");
        WordCountResultCommand wordCountResultCommandMock = mock(WordCountResultCommand.class);
        Optional<Word> optionalWord = Optional.of(wordMock);

        when(wordRepository.findByWord(anyString())).thenReturn(optionalWord);
        when(modelMapper.map(any(Word.class), eq(WordCountResultCommand.class))).thenReturn(wordCountResultCommandMock);

        WordCountResultCommand result = defaultWordService.getWordCount("existing");
        assertThat(result, equalTo(wordCountResultCommandMock));
    }

    @Test
    public void getWordCountNotExisting() {
        Optional<Word> optionalWordMock = Optional.empty();

        when(wordRepository.findByWord(anyString())).thenReturn(optionalWordMock);

        WordCountResultCommand result = defaultWordService.getWordCount("word");

        assertThat(result, notNullValue());
        assertThat(result.getCounter(), equalTo(0L));
        assertThat(result.getWord(), equalTo("word"));
    }

    @Test
    public void addWordExisting() throws GenericServiceException {
        Word wordMock = mock(Word.class);
        Optional<Word> optionalWord = Optional.of(wordMock);
        WordCommand wordCommandMock = mock(WordCommand.class);

        when(wordCommandMock.getWord()).thenReturn("word");
        when(wordRepository.findByWord(anyString())).thenReturn(optionalWord);

        defaultWordService.addWord(wordCommandMock);

        verify(wordMock, times(1)).incrementCounter();
        verify(wordRepository, times(1)).save(wordMock);
    }

    @Test
    public void addWordNew() throws GenericServiceException {
        ArgumentCaptor<Word> wordArgumentCaptor = ArgumentCaptor.forClass(Word.class);
        Optional<Word> optionalWord = Optional.empty();
        WordCommand wordCommandMock = mock(WordCommand.class);

        when(wordCommandMock.getWord()).thenReturn("word");
        when(wordRepository.findByWord(anyString())).thenReturn(optionalWord);

        defaultWordService.addWord(wordCommandMock);

        verify(wordRepository, times(1)).save(wordArgumentCaptor.capture());
        Word result = wordArgumentCaptor.getValue();

        assertThat(result, notNullValue());
        assertThat(result.getCounter(), equalTo(1L));
        assertThat(result.getWord(), equalTo("word"));
    }


    @Test(expected = GenericServiceException.class)
    public void addWordNull() throws GenericServiceException {
        defaultWordService.addWord(null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void addWordBadPattern() throws GenericServiceException {
        WordCommand wordCommandMock = mock(WordCommand.class);

        when(wordCommandMock.getWord()).thenReturn("string");
        when(wordRepository.findByWord(anyString())).thenThrow(ConstraintViolationException.class);

        defaultWordService.addWord(wordCommandMock);
    }
}
