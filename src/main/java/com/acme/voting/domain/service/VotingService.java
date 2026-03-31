package com.acme.voting.domain.service;

import com.acme.voting.domain.exception.EntityAlreadyExistsException;
import com.acme.voting.domain.exception.EntityNotFoundException;
import com.acme.voting.domain.model.Candidate;
import com.acme.voting.domain.model.CandidateKey;
import com.acme.voting.domain.model.Party;
import com.acme.voting.infrastructure.repository.InMemoryCandidateRepository;
import com.acme.voting.infrastructure.repository.InMemoryPartyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Optional;

/**
 * Serviço que encapsula a lógica de negócio do sistema de votação.
 */
public class VotingService {

    private static final Logger log = LoggerFactory.getLogger(VotingService.class);

    private final InMemoryPartyRepository partyRepository;
    private final InMemoryCandidateRepository candidateRepository;

    public VotingService(InMemoryPartyRepository partyRepo, InMemoryCandidateRepository candidateRepo) {
        this.partyRepository = partyRepo;
        this.candidateRepository = candidateRepo;
    }

    /**
     * Cadastra um novo partido.
     * @throws EntityAlreadyExistsException se o partido já existir.
     */
    public void registerParty(Party party) {
        if (partyRepository.findById(party.getNumber()).isPresent()) {
            throw new EntityAlreadyExistsException("Partido", party.getNumber());
        }
        partyRepository.save(party);
        log.info("Partido registrado: {}", party.getName());
    }

    /**
     * Cadastra um novo candidato.
     * @throws EntityAlreadyExistsException se o candidato já existir.
     */
    public void registerCandidate(Candidate candidate) {
        CandidateKey key = new CandidateKey(candidate.getNumber(), candidate.getMunicipality());
        if (candidateRepository.findById(key).isPresent()) {
            throw new EntityAlreadyExistsException("Candidato", candidate.getNumber());
        }
        candidateRepository.save(candidate);
        candidate.getParty().addCandidate(candidate);
        log.info("Candidato registrado: {} ({})", candidate.getName(), candidate.getMunicipality());
    }

    /**
     * Adiciona votos a um candidato específico em um município.
     */
    public void addVotes(int number, String municipality, int votes) {
        getCandidateByNumberAndMunicipality(number, municipality)
                .ifPresentOrElse(
                    c -> c.addVotes(votes),
                    () -> log.warn("Tentativa de votar em candidato inexistente: {} em {}", number, municipality)
                );
    }

    public Optional<Party> getPartyByNumber(int number) {
        return partyRepository.findById(number);
    }

    public Optional<Party> getPartyByName(String name) {
        return partyRepository.findByName(name);
    }

    /**
     * Busca um candidato pelo número e município com performance O(1).
     */
    public Optional<Candidate> getCandidateByNumberAndMunicipality(int number, String municipality) {
        return candidateRepository.findById(new CandidateKey(number, municipality));
    }

    /**
     * Retorna o partido que possui o maior número de candidatos cadastrados.
     */
    public Optional<Party> getPartyWithMostCandidates() {
        return partyRepository.findAll().stream()
                .max(Comparator.comparingInt(p -> p.getCandidates().size()));
    }

    /**
     * Retorna o vereador mais votado.
     */
    public Optional<Candidate> getMostVotedCouncilor() {
        return findMostVotedByFilter(Candidate::isCouncilor);
    }

    /**
     * Retorna o prefeito mais votado.
     */
    public Optional<Candidate> getMostVotedMayor() {
        return findMostVotedByFilter(Candidate::isMayor);
    }

    private Optional<Candidate> findMostVotedByFilter(java.util.function.Predicate<Candidate> filter) {
        return candidateRepository.findAll().stream()
                .filter(filter)
                .max(Comparator.comparingInt(Candidate::getVotos));
    }
}
