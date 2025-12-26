package com.commandline.app;

import java.util.Scanner;

import com.commandline.app.util.CommandProcessor;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandProcessor processor = new CommandProcessor();
        System.out.println("Welcome to the Command Line Application!");
        System.out.println("Type 'exit' to quit.");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            if (input.equalsIgnoreCase("help")) {
                processor.printAvailableCommands();
                continue;
            }
            processor.process(input);
        }
        scanner.close();
        System.out.println("Goodbye!");
    }
}