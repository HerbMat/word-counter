package com.solid.wordcounter.exception;

/**
 * Exception for problems with word bad format.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
public class GenericServiceException extends Exception {

    public GenericServiceException(String message) {
        super(message);
    }
}
