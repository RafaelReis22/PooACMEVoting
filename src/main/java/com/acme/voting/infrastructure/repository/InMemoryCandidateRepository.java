package com.acme.voting.infrastructure.repository;

import com.acme.voting.domain.model.Candidate;
import com.acme.voting.domain.model.CandidateKey;
import com.acme.voting.domain.repository.Repository;

import java.util.*;

/**
 * Implementação em memória do repositório de Candidatos.
 * Otimizado para buscas por número e município.
 */
public class InMemoryCandidateRepository implements Repository<Candidate, CandidateKey> {
    private final Map<CandidateKey, Candidate> candidates = new HashMap<>();

    @Override
    public void save(Candidate candidate) {
        CandidateKey key = new CandidateKey(candidate.getNumber(), candidate.getMunicipality());
        candidates.put(key, candidate);
    }

    @Override
    public Optional<Candidate> findById(CandidateKey key) {
        return Optional.ofNullable(candidates.get(key));
    }

    @Override
    public Collection<Candidate> findAll() {
        return Collections.unmodifiableCollection(candidates.values());
    }
}
