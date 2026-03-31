package com.acme.voting.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa um Partido Político no sistema ACME Voting.
 */
public class Party {
    private final int number;
    private final String name;
    private final List<Candidate> candidates;

    public Party(int number, String name) {
        if (number <= 0) throw new IllegalArgumentException("Número do partido deve ser positivo.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome do partido é obrigatório.");
        this.number = number;
        this.name = name;
        this.candidates = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public List<Candidate> getCandidates() {
        return Collections.unmodifiableList(candidates);
    }

    public void addCandidate(Candidate candidate) {
        if (candidate != null && !candidates.contains(candidate)) {
            candidates.add(candidate);
        }
    }

    @Override
    public String toString() {
        return String.format("%d,%s", number, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return number == party.number;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(number);
    }
}
