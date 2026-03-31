package com.acme.voting.domain.model;

/**
 * Chave composta que identifica unicamente um candidato por seu número e município.
 * Essencial para buscas otimizadas com performance O(1).
 */
public record CandidateKey(int number, String municipality) {}
