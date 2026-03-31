package com.acme.voting.infrastructure.repository;

import com.acme.voting.domain.model.Party;
import com.acme.voting.domain.repository.Repository;

import java.util.*;

/**
 * Implementação em memória do repositório de Partidos.
 */
public class InMemoryPartyRepository implements Repository<Party, Integer> {
    private final Map<Integer, Party> partiesByNameOrNumber = new HashMap<>();
    private final Map<String, Party> partiesByName = new HashMap<>();

    @Override
    public void save(Party party) {
        partiesByNameOrNumber.put(party.getNumber(), party);
        partiesByName.put(party.getName().toLowerCase(), party);
    }

    @Override
    public Optional<Party> findById(Integer number) {
        return Optional.ofNullable(partiesByNameOrNumber.get(number));
    }

    public Optional<Party> findByName(String name) {
        return Optional.ofNullable(partiesByName.get(name.toLowerCase()));
    }

    @Override
    public Collection<Party> findAll() {
        return Collections.unmodifiableCollection(partiesByNameOrNumber.values());
    }
}
