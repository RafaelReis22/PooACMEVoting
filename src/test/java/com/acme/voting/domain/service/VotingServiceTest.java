package com.acme.voting.domain.service;

import com.acme.voting.domain.exception.EntityAlreadyExistsException;
import com.acme.voting.domain.model.Candidate;
import com.acme.voting.domain.model.Party;
import com.acme.voting.infrastructure.repository.InMemoryCandidateRepository;
import com.acme.voting.infrastructure.repository.InMemoryPartyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VotingServiceTest {

    private VotingService votingService;
    private Party party11;

    @BeforeEach
    void setUp() {
        InMemoryPartyRepository partyRepo = new InMemoryPartyRepository();
        InMemoryCandidateRepository candRepo = new InMemoryCandidateRepository();
        votingService = new VotingService(partyRepo, candRepo);

        party11 = new Party(11, "Party Eleven");
        votingService.registerParty(party11);
    }

    @Test
    void shouldRegisterCandidate() {
        Candidate candidate = new Candidate(123, "John Doe", "Anytown", party11);
        
        assertThatCode(() -> votingService.registerCandidate(candidate))
                .doesNotThrowAnyException();

        assertThat(votingService.getCandidateByNumberAndMunicipality(123, "Anytown")).isPresent();
        assertThat(party11.getCandidates()).contains(candidate);
    }

    @Test
    void shouldNotRegisterDuplicateCandidate() {
        Candidate candidate1 = new Candidate(123, "John Doe", "Anytown", party11);
        Candidate candidate2 = new Candidate(123, "Jane Doe", "Anytown", party11);

        votingService.registerCandidate(candidate1);
        
        assertThatThrownBy(() -> votingService.registerCandidate(candidate2))
                .isInstanceOf(EntityAlreadyExistsException.class);
    }

    @Test
    void shouldAccumulateVotes() {
        Candidate candidate = new Candidate(123, "John Doe", "Anytown", party11);
        votingService.registerCandidate(candidate);

        votingService.addVotes(123, "Anytown", 100);
        votingService.addVotes(123, "Anytown", 50);

        assertThat(candidate.getVotos()).isEqualTo(150);
    }

    @Test
    void shouldIdentifyMostVotedMayorAndCouncilor() {
        Candidate councilorA = new Candidate(100, "Councilor A", "M1", party11); // < 10000
        Candidate councilorB = new Candidate(200, "Councilor B", "M1", party11);
        Candidate mayorA = new Candidate(10000, "Mayor A", "M1", party11); // >= 10000
        Candidate mayorB = new Candidate(10010, "Mayor B", "M2", party11);

        votingService.registerCandidate(councilorA);
        votingService.registerCandidate(councilorB);
        votingService.registerCandidate(mayorA);
        votingService.registerCandidate(mayorB);

        councilorA.addVotes(50);
        councilorB.addVotes(100);
        mayorA.addVotes(500);
        mayorB.addVotes(300);

        assertThat(votingService.getMostVotedCouncilor().get()).isEqualTo(councilorB);
        assertThat(votingService.getMostVotedMayor().get()).isEqualTo(mayorA);
    }
}
