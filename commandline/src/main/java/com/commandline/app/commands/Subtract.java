package com.commandline.app.commands;

import java.math.BigDecimal;

import com.commandline.app.annotation.CommandPath;
import com.commandline.app.commands.api.Command;

@CommandPath(path = "subtract")
public class Subtract implements Command {

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide at least two numbers to subtract.");
            return;
        }
        try {
            BigDecimal result = new BigDecimal(args[0]);
            for (int i = 1; i < args.length; i++) {
                result = result.subtract(new BigDecimal(args[i]));
            }
            System.out.println("Result: " + result);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }
    
}
