package com.solid.wordcounter.controllers;

import com.solid.wordcounter.commands.WordCommand;
import com.solid.wordcounter.commands.WordCountResultCommand;
import com.solid.wordcounter.exception.GenericServiceException;
import com.solid.wordcounter.services.WordService;
import io.swagger.annotations.*;
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
@Api(value="word counter", description="Operations managing words in database")
public class WordController {

    WordService wordService;

    @ApiOperation(value = "Add new word to database", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added word."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addWord(
            @Valid
            @ApiParam(value = "Request body with word which we want to add.")
            @RequestBody WordCommand wordCommand) throws GenericServiceException {
            wordService.addWord(wordCommand);
    }

    @ApiOperation(value = "Retrieve information about how many times word was added", response = WordCountResultCommand.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved word information."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @GetMapping("/getCount/{word}")
    @ResponseStatus(HttpStatus.OK)
    public WordCountResultCommand getWordCount(
            @Valid
            @NotBlank(message = "{word.not.blank}")
            @Pattern(regexp = "^[A-Za-z]*$", message = "{word.bad.pattern}")
            @ApiParam(value = "Word for which we want retrieve counter.")
            @PathVariable String word ) throws GenericServiceException {
        return wordService.getWordCount(word);
    }
}
