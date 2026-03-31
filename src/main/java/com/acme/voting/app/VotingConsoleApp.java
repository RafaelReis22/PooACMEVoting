package com.acme.voting.app;

import com.acme.voting.domain.model.Candidate;
import com.acme.voting.domain.model.Party;
import com.acme.voting.domain.service.VotingService;
import com.acme.voting.infrastructure.repository.InMemoryCandidateRepository;
import com.acme.voting.infrastructure.repository.InMemoryPartyRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

/**
 * Orquestrador principal da aplicação para a interface CLI/Arquivo.
 */
public class VotingConsoleApp {

    private final VotingService service;
    private final String inputPath;
    private final String outputPath;

    public VotingConsoleApp(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.service = new VotingService(new InMemoryPartyRepository(), new InMemoryCandidateRepository());
    }

    /**
     * Executa o processamento principal do sistema de votação.
     */
    public void run() {
        try (Scanner scanner = new Scanner(new File(inputPath), StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(new File(outputPath), StandardCharsets.UTF_8)) {

            scanner.useLocale(Locale.ENGLISH);

            processParties(scanner, writer);
            processCandidates(scanner, writer);
            processVotes(scanner, writer);
            processQueries(scanner, writer);

        } catch (IOException e) {
            System.err.println("Erro fatal: " + e.getMessage());
        }
    }

    private void processParties(Scanner scanner, PrintWriter writer) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("-1")) break;

            int number = Integer.parseInt(line);
            String name = scanner.nextLine().trim();

            Party party = new Party(number, name);
            try {
                service.registerParty(party);
                writer.println("1:" + party);
            } catch (com.acme.voting.domain.exception.EntityAlreadyExistsException e) {
                writer.println("1:Partido já cadastrado.");
            }
        }
    }

    private void processCandidates(Scanner scanner, PrintWriter writer) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("-1")) break;

            int number = Integer.parseInt(line);
            String name = scanner.nextLine().trim();
            String municipality = scanner.nextLine().trim();

            // Nota: Conforme lógica do projeto original, os candidatos são vinculados ao partido 11.
            service.getPartyByNumber(11).ifPresentOrElse(party -> {
                Candidate candidate = new Candidate(number, name, municipality, party);
                try {
                    service.registerCandidate(candidate);
                    writer.println("2:" + number + "," + name + "," + municipality);
                } catch (com.acme.voting.domain.exception.EntityAlreadyExistsException e) {
                    writer.println("2:Candidato já cadastrado.");
                }
            }, () -> writer.println("2:Partido não encontrado."));
        }
    }

    private void processVotes(Scanner scanner, PrintWriter writer) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("-1")) break;

            int number = Integer.parseInt(line);
            String municipality = scanner.nextLine().trim();
            int votes = Integer.parseInt(scanner.nextLine().trim());

            service.addVotes(number, municipality, votes);
            writer.println("3:" + number + "," + municipality + "," + votes);
        }
    }

    private void processQueries(Scanner scanner, PrintWriter writer) {
        // Consulta 4: Informações do Partido
        if (scanner.hasNextLine()) {
            int partyNum = Integer.parseInt(scanner.nextLine().trim());
            service.getPartyByNumber(partyNum).ifPresentOrElse(
                    p -> writer.println("4:" + p.getNumber() + "," + p.getName()),
                    () -> writer.println("4:Partido não encontrado.")
            );
        }

        // Consulta 5: Informações do Candidato
        if (scanner.hasNextLine()) {
            int candNum = Integer.parseInt(scanner.nextLine().trim());
            if (scanner.hasNextLine()) {
                String mun = scanner.nextLine().trim();
                service.getCandidateByNumberAndMunicipality(candNum, mun).ifPresentOrElse(
                        c -> writer.println("5:" + c.getNumber() + "," + c.getName() + "," + c.getMunicipality() + "," + c.getVotos()),
                        () -> writer.println("5:Candidato não encontrado.")
                );
            }
        }

        // Consulta 6: Listagem de candidatos do partido
        if (scanner.hasNextLine()) {
            String partyName = scanner.nextLine().trim();
            service.getPartyByName(partyName).ifPresentOrElse(p -> {
                StringBuilder sb = new StringBuilder("6:").append(p.getName());
                p.getCandidates().stream().findFirst().ifPresent(c -> {
                    sb.append(",").append(c.getNumber()).append(",").append(c.getName())
                            .append(",").append(c.getMunicipality()).append(",").append(c.getVotos());
                });
                writer.println(sb);
            }, () -> writer.println("6:Partido não encontrado."));
        }

        // Consulta 7: Partido com mais candidatos
        service.getPartyWithMostCandidates().ifPresentOrElse(
                p -> writer.println("7:" + p.getNumber() + "," + p.getName() + "," + p.getCandidates().size()),
                () -> writer.println("7:Partido com nenhum candidato.")
        );

        // Consulta 8: Prefeito e Vereador mais votados
        service.getMostVotedCouncilor().ifPresent(c ->
                writer.println("8:" + c.getNumber() + "," + c.getName() + "," + c.getMunicipality() + "," + c.getVotos()));

        service.getMostVotedMayor().ifPresent(c ->
                writer.println("8:" + c.getNumber() + "," + c.getName() + "," + c.getMunicipality() + "," + c.getVotos()));
    }
}
