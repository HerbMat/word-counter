package com.solid.wordcounter.services;

import com.solid.wordcounter.commands.WordCommand;
import com.solid.wordcounter.commands.WordCountResultCommand;
import com.solid.wordcounter.domain.Word;
import com.solid.wordcounter.exception.GenericServiceException;
import com.solid.wordcounter.repositories.WordRepository;
import com.solid.wordcounter.services.impl.DefaultWordService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordServiceIntegrationTest {

    @Autowired
    WordRepository wordRepository;

    @Autowired
    ModelMapper modelMapper;

    WordService wordService;

    @Before
    public void setUp() throws Exception {
        wordService = new DefaultWordService(wordRepository, modelMapper);
        insertTestWordToDatabase("word", 3L);
    }

    @Test
    public void getWordCountExisting() throws GenericServiceException {
        WordCountResultCommand result = wordService.getWordCount("word");

        assertThat(result, notNullValue());
        assertThat(result.getCounter(), equalTo(3L));
        assertThat(result.getWord(), equalTo("word"));
    }

    @Test
    public void getWordCountNotExisting() throws GenericServiceException {
        WordCountResultCommand result = wordService.getWordCount("notExisting");

        assertThat(result, notNullValue());
        assertThat(result.getCounter(), equalTo(0L));
        assertThat(result.getWord(), equalTo("notExisting"));
    }

    @Test
    public void addWordNotExisting() throws GenericServiceException {
        WordCommand wordCommand = WordCommand.builder()
                .word("new")
                .build();
        wordService.addWord(wordCommand);

        Optional<Word> resultOptional = wordRepository.findByWord("new");

        assertThat(resultOptional.isPresent(), is(true));
        Word result = resultOptional.get();
        assertThat(result.getCounter(), equalTo(1L));
        assertThat(result.getWord(), equalTo("new"));
    }

    @Test
    public void addWordExisting() throws GenericServiceException {
        WordCommand wordCommand = WordCommand.builder()
                .word("word")
                .build();
        wordService.addWord(wordCommand);

        Optional<Word> resultOptional = wordRepository.findByWord("word");

        assertThat(resultOptional.isPresent(), is(true));
        Word result = resultOptional.get();
        assertThat(result.getCounter(), equalTo(4L));
        assertThat(result.getWord(), equalTo("word"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void addWordException() throws GenericServiceException {
        WordCommand wordCommand = WordCommand.builder()
                .word(" 9")
                .build();
        wordService.addWord(wordCommand);
    }

    @Test(expected = GenericServiceException.class)
    public void addWordExceptionNullCommand() throws GenericServiceException {
        wordService.addWord(null);
    }

    @After
    public void tearDown() {
        Optional<Word> wordOptional = wordRepository.findByWord("word");
        if (wordOptional.isPresent()) {
            wordRepository.delete(wordOptional.get());
        }

    }

    private void insertTestWordToDatabase(String word, long counter) {
        Word wordEntity = Word.builder()
                .word(word)
                .counter(counter)
                .build();
        wordRepository.save(wordEntity);
    }
}
