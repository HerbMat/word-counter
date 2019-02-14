package com.solid.wordcounter.commands;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * Command which presents counter of inserting given word for rest api.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordCountResultCommand implements Serializable {

    @ApiModelProperty(notes = "Word for which is returned counter.")
    private String word;

    @ApiModelProperty(notes = "Number of attempts to add word.")
    private long counter;
}
