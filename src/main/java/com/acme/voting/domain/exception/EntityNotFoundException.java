package com.acme.voting.domain.exception;

/**
 * Exceção lançada quando uma entidade não é encontrada no sistema.
 */
public class EntityNotFoundException extends VotingException {
    public EntityNotFoundException(String entity, Object identifier) {
        super(String.format("%s com identificador %s não encontrado(a).", entity, identifier));
    }
}
