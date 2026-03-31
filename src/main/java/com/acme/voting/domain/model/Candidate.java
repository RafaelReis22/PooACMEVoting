package com.acme.voting.domain.model;

import java.util.Objects;

/**
 * Representa um Candidato no sistema ACME Voting.
 * Baseado nas regras de negócio:
 * - Número < 10000: Vereador
 * - Número >= 10000: Prefeito
 */
public class Candidate {
    private static final int MAYOR_THRESHOLD = 10000;

    private final int number;
    private final String name;
    private final String municipality;
    private final Party party;
    private int votes;

    public Candidate(int number, String name, String municipality, Party party) {
        if (number <= 0) throw new IllegalArgumentException("Número do candidato deve ser positivo.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome do candidato é obrigatório.");
        if (municipality == null || municipality.isBlank()) throw new IllegalArgumentException("Município é obrigatório.");
        if (party == null) throw new IllegalArgumentException("Partido é obrigatório.");
        
        this.number = number;
        this.name = name;
        this.municipality = municipality;
        this.party = party;
        this.votes = 0;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getMunicipality() {
        return municipality;
    }

    public Party getParty() {
        return party;
    }

    public int getVotos() {
        return votes;
    }

    public void addVotes(int votos) {
        if (votos > 0) {
            this.votes += votos;
        }
    }

    public boolean isMayor() {
        return number >= MAYOR_THRESHOLD;
    }

    public boolean isCouncilor() {
        return number < MAYOR_THRESHOLD;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%d", number, name, municipality, votes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate that = (Candidate) o;
        return number == that.number && Objects.equals(municipality, that.municipality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, municipality);
    }
}
