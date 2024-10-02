package com.acme.voting.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CandidateTest {

    private final Party party = new Party(11, "Test Party");

    @Test
    void shouldCreateValidCandidate() {
        Candidate c = new Candidate(100, "Ana Silva", "São Paulo", party);
        assertThat(c.getNumber()).isEqualTo(100);
        assertThat(c.getName()).isEqualTo("Ana Silva");
        assertThat(c.getMunicipality()).isEqualTo("São Paulo");
        assertThat(c.getVotos()).isZero();
    }

    @Test
    void shouldClassifyCouncilorWhenNumberBelow10000() {
        Candidate c = new Candidate(999, "Vereador", "SP", party);
        assertThat(c.isCouncilor()).isTrue();
        assertThat(c.isMayor()).isFalse();
    }

    @Test
    void shouldClassifyMayorWhenNumberAtOrAbove10000() {
        Candidate c = new Candidate(10000, "Prefeito", "SP", party);
        assertThat(c.isMayor()).isTrue();
        assertThat(c.isCouncilor()).isFalse();
    }

    @Test
    void shouldAccumulateVotes() {
        Candidate c = new Candidate(1, "Teste", "RJ", party);
        c.addVotes(50);
        c.addVotes(30);
        assertThat(c.getVotos()).isEqualTo(80);
    }

    @Test
    void shouldIgnoreNegativeOrZeroVotes() {
        Candidate c = new Candidate(1, "Teste", "RJ", party);
        c.addVotes(-10);
        c.addVotes(0);
        assertThat(c.getVotos()).isZero();
    }

    @Test
    void shouldThrowWhenNumberIsZeroOrNegative() {
        assertThatThrownBy(() -> new Candidate(0, "Nome", "SP", party))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Candidate(-1, "Nome", "SP", party))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() -> new Candidate(1, "", "SP", party))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Candidate(1, "  ", "SP", party))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenPartyIsNull() {
        assertThatThrownBy(() -> new Candidate(1, "Nome", "SP", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equalityShouldBeByNumberAndMunicipality() {
        Candidate c1 = new Candidate(100, "João", "SP", party);
        Candidate c2 = new Candidate(100, "Maria", "SP", party);
        Candidate c3 = new Candidate(100, "João", "RJ", party);
        assertThat(c1).isEqualTo(c2);
        assertThat(c1).isNotEqualTo(c3);
    }
}
