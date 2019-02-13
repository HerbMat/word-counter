package com.solid.wordcounter.commands;

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
    String word;
    long counter;
}
