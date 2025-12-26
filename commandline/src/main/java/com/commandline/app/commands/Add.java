package com.commandline.app.commands;

import java.math.BigDecimal;

import com.commandline.app.annotation.CommandPath;

@CommandPath(path = "add")
public class Add implements Command {

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public void execute(String[] args) {
        BigDecimal sum = BigDecimal.ZERO;

         if (args.length < 2) {
            System.out.println("Please provide at least two numbers to add.");
            return;
        }

        for (String arg : args) {
            try {
                BigDecimal number = new BigDecimal(arg);
                sum = sum.add(number);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number: " + arg);
            }
        }
        System.out.println("Sum: " + sum);
    }
    
}
