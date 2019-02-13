package com.solid.wordcounter.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration with beans necessary for word application.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@Configuration
public class WordConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
