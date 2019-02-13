package com.solid.wordcounter.controllers;

import com.solid.wordcounter.commands.WordCommand;
import com.solid.wordcounter.commands.WordCountResultCommand;
import com.solid.wordcounter.exception.GenericServiceException;
import com.solid.wordcounter.services.WordService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Rest controller with basic operations for managing words.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/word")
@Validated
public class WordController {

    WordService wordService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addWord(@Valid @RequestBody WordCommand wordCommand) throws GenericServiceException {
            wordService.addWord(wordCommand);
    }

    @GetMapping("/getCount/{word}")
    @ResponseStatus(HttpStatus.OK)
    public WordCountResultCommand getWordCount(
            @Valid
            @NotBlank(message = "{word.not.blank}")
            @Pattern(regexp = "^[A-Za-z]*$", message = "{word.bad.pattern}")
            @PathVariable String word ) throws GenericServiceException {
        return wordService.getWordCount(word);
    }
}
