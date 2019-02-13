package com.solid.wordcounter.services.impl;

import com.solid.wordcounter.commands.WordCommand;
import com.solid.wordcounter.commands.WordCountResultCommand;
import com.solid.wordcounter.domain.Word;
import com.solid.wordcounter.exception.GenericServiceException;
import com.solid.wordcounter.repositories.WordRepository;
import com.solid.wordcounter.services.WordService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * Default implementation of {@link WordService}
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@Service
@AllArgsConstructor
@Log4j2
public class DefaultWordService implements WordService {

    private static final long WORD_NOT_EXISTS_COUNTER = 0L;

    private WordRepository wordRepository;
    private ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    public WordCountResultCommand getWordCount(String word) {
        Optional<Word> foundWord = wordRepository.findByWord(word);
        if (foundWord.isPresent()) {
            log.debug("Found word {} in database.", word);
            return modelMapper.map(foundWord.get(), WordCountResultCommand.class);
        }
        log.debug("Word {} was not found in database.", word);
        return WordCountResultCommand.builder()
                .counter(WORD_NOT_EXISTS_COUNTER)
                .word(word)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    public void addWord(WordCommand wordCommand) throws GenericServiceException {
        if (wordCommand == null) {
            log.error("There was null command.");
            throw new GenericServiceException("Word command cannot be null");
        }
        log.debug("Preparing to save word", wordCommand.getWord());
        try {
            wordRepository.save(prepareEntityForSaveToDatabase(wordCommand));
        } catch (ConstraintViolationException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private Word prepareEntityForSaveToDatabase(WordCommand wordCommand) {
        Optional<Word> wordOptional = wordRepository.findByWord(wordCommand.getWord());
        if (wordOptional.isPresent()) {
            log.debug("Word {} was found in database. Incrementing the count", wordCommand.getWord());
            Word wordEntity = wordOptional.get();
            wordEntity.incrementCounter();

            return wordEntity;
        }
        log.debug("Word {} was not found in database. Creating new.", wordCommand.getWord());
        return Word.builder()
                .word(wordCommand.getWord())
                .build();
    }
}
