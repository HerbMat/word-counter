package com.solid.wordcounter.repositories;

import com.solid.wordcounter.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository managing words in database
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWord(String word);
}
