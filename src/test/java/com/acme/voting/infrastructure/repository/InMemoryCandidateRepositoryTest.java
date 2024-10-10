package com.acme.voting.infrastructure.repository;

import com.acme.voting.domain.model.Candidate;
import com.acme.voting.domain.model.CandidateKey;
import com.acme.voting.domain.model.Party;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InMemoryCandidateRepositoryTest {

    private InMemoryCandidateRepository repo;
    private final Party party = new Party(11, "Partido Teste");

    @BeforeEach
    void setUp() {
        repo = new InMemoryCandidateRepository();
    }

    @Test
    void shouldSaveAndFindById() {
        Candidate c = new Candidate(100, "Rafael", "SP", party);
        repo.save(c);
        assertThat(repo.findById(new CandidateKey(100, "SP"))).contains(c);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repo.findById(new CandidateKey(999, "RJ"))).isEmpty();
    }

    @Test
    void shouldReturnAllSavedCandidates() {
        repo.save(new Candidate(1, "A", "SP", party));
        repo.save(new Candidate(2, "B", "RJ", party));
        assertThat(repo.findAll()).hasSize(2);
    }

    @Test
    void shouldOverwriteExistingKeyOnSave() {
        Candidate original = new Candidate(100, "Original", "SP", party);
        Candidate updated = new Candidate(100, "Updated", "SP", party);
        repo.save(original);
        repo.save(updated);
        assertThat(repo.findAll()).hasSize(1);
        assertThat(repo.findById(new CandidateKey(100, "SP"))).contains(updated);
    }

    @Test
    void sameCandidateNumberInDifferentMunicipalitiesAreDistinct() {
        Candidate sp = new Candidate(100, "Cand SP", "SP", party);
        Candidate rj = new Candidate(100, "Cand RJ", "RJ", party);
        repo.save(sp);
        repo.save(rj);
        assertThat(repo.findAll()).hasSize(2);
    }
}
