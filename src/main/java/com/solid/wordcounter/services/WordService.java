package com.solid.wordcounter.services;

import com.solid.wordcounter.commands.WordCommand;
import com.solid.wordcounter.commands.WordCountResultCommand;
import com.solid.wordcounter.exception.GenericServiceException;

/**
 * Service for managing word database.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
public interface WordService {

    /**
     * It returns number of tries of adding new word.
     * if the word does not exists then it returns command with counter 0.
     *
     * @param word given word
     * @return command with number of tries of adding new world
     * @throws GenericServiceException
     */
    WordCountResultCommand getWordCount(String word) throws GenericServiceException;

    /**
     * It adds new word to database. If word does not exists then creates new entry in database.
     * Otherwise it increment counter in database entry for given word.
     * @param wordCommand word to add.
     * @throws GenericServiceException
     */
    void addWord(WordCommand wordCommand) throws GenericServiceException;
}
