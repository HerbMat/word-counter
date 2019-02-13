package com.solid.wordcounter.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Command for adding new word to database.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordCommand implements Serializable {

    @NotBlank(message = "{word.not.blank}")
    @Pattern(regexp = "^[A-Za-z]*$", message = "{word.bad.pattern}")
    String word;
}
