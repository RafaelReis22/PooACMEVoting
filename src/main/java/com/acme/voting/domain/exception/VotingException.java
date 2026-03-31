package com.acme.voting.domain.exception;

/**
 * Exceção base para o sistema de votação ACME.
 */
public abstract class VotingException extends RuntimeException {
    public VotingException(String message) {
        super(message);
    }
}
