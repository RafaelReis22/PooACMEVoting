package com.acme.voting.app;

/**
 * Ponto de entrada para o sistema ACME Voting.
 */
public class Main {
    public static void main(String[] args) {
        String input = "input.txt";
        String output = "output.txt";

        if (args.length >= 2) {
            input = args[0];
            output = args[1];
        }

        new VotingConsoleApp(input, output).run();
    }
}
