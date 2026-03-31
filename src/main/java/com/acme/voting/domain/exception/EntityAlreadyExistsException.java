package com.acme.voting.domain.exception;

/**
 * Exceção lançada quando uma entidade já existe no sistema.
 */
public class EntityAlreadyExistsException extends VotingException {
    public EntityAlreadyExistsException(String entity, Object identifier) {
        super(String.format("%s com identificador %s já cadastrado(a).", entity, identifier));
    }
}
