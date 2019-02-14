package com.solid.wordcounter.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Entity representing stored words in database.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@Builder
@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "{word.not.blank}")
    @Pattern(regexp = "^[A-Za-z]*$", message = "{word.bad.pattern}")
    private String word;

    @Min(value = 0L, message = "{counter.not.positive}")
    @Builder.Default private long counter = 1L;

    public void incrementCounter() {
        ++counter;
    }
}
