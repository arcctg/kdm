package org.arcctg;

import java.util.Scanner;

class SimplifierApp {
    private final Scanner scanner;
    private final ExpressionProcessor processor;

    public SimplifierApp() {
        this.scanner = new Scanner(System.in);
        this.processor = new ExpressionProcessor();
    }

    public void run() {
        printInstructions();

        String input;

        while (!(isExitCommand(input = scanner.nextLine()))) {
            processor.process(input);
            promptNextAction();
        }
    }

    private void printInstructions() {
        System.out.println("""
                Boolean Expression Simplifier
                
                Enter mode (0 for PCNF, 1 for PDNF) with numbers separated by a space or 'exit' to terminate the program.
                
                Examples:
                If you want to find PCNF on sets 1, 3, 4, 5, 7, 8, 11, 13, 14, 15, enter:
                0 1 3 4 5 7 8 11 13 14 15
                
                If you want to find the PDNF on sets 0, 2, 6, 9, 10, 12, enter:
                1 0 2 6 9 10 12
                
                If you want to terminate the program, enter:
                exit
                """);
    }

    private boolean isExitCommand(String input) {
        return "exit".equalsIgnoreCase(input.trim());
    }

    private void promptNextAction() {
        System.out.println("\nEnter your query or 'exit' to terminate the program.\n");
    }
}