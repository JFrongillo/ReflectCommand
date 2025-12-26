package com.commandline.app.util;

import com.commandline.app.annotation.CommandPath;
import com.commandline.app.commands.Command;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private final Map<String, Class<? extends Command>> commandRegistry = new HashMap<>();

    public CommandProcessor() {
        scanAndRegisterCommands();
    }

    private void scanAndRegisterCommands() {
        // Start a try block to handle any exceptions during the scanning process
        try {
            // Define the package name where all command classes are located
            String packageName = "com.commandline.app.commands";
            // Convert the package name to a file path format by replacing dots with slashes
            String path = packageName.replace('.', '/');
            // Get the ClassLoader for the current thread to load classes and resources
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // Get the URL to the directory containing the command classes
            URL resource = classLoader.getResource(path);
            
            // Check if the resource (commands package directory) was found
            if (resource == null) {
                // Print a warning message if the commands package cannot be located
                System.err.println("Warning: Could not find commands package");
                // Exit the method early since there's nothing to scan
                return;
            }

            // Convert the URL to a File object representing the directory
            File directory = new File(resource.getFile());
            // Check if the directory actually exists on the filesystem
            if (!directory.exists()) {
                // Exit the method if the directory doesn't exist
                return;
            }

            // List all files in the directory that end with ".class" extension using a lambda filter
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
            // Check if the listFiles operation returned null (can happen if directory is not readable)
            if (files == null) {
                // Exit the method if no files array was returned
                return;
            }

            // Iterate through each .class file found in the directory
            for (File file : files) {
                // Build the fully qualified class name by combining package name and filename (minus .class extension)
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                // Start a try block to handle errors when loading individual classes
                try {
                    // Load the class using its fully qualified name
                    Class<?> clazz = Class.forName(className);
                    
                    // Check if the class implements the Command interface AND has the @CommandPath annotation
                    if (Command.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(CommandPath.class)) {
                        // Get the @CommandPath annotation from the class
                        CommandPath annotation = clazz.getAnnotation(CommandPath.class);
                        // Extract the path value from the annotation (e.g., "add", "subtract")
                        String commandPath = annotation.path();
                        
                        // Suppress unchecked cast warning since we've already verified it's a Command subclass
                        @SuppressWarnings("unchecked")
                        // Cast the generic Class<?> to the specific Command type
                        Class<? extends Command> commandClass = (Class<? extends Command>) clazz;
                        // Store the command class in the registry map using the path as the key
                        commandRegistry.put(commandPath, commandClass);
                        
                        // Print a confirmation message showing which command was registered
                        System.out.println("Registered command: " + commandPath + " -> " + clazz.getSimpleName());
                    }
                // Catch any ClassNotFoundException thrown by Class.forName()
                } catch (ClassNotFoundException e) {
                    // Print an error message if a class file cannot be loaded
                    System.err.println("Could not load class: " + className);
                }
            }
        // Catch any other exceptions that might occur during the scanning process
        } catch (Exception e) {
            // Print a generic error message with the exception details
            System.err.println("Error scanning for commands: " + e.getMessage());
        }
    }

    public void process(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Please enter a command.");
            return;
        }

        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        Class<? extends Command> commandClass = commandRegistry.get(commandName);
        
        if (commandClass == null) {
            System.out.println("Unknown command: " + commandName);
            System.out.println("Available commands: " + commandRegistry.keySet());
            return;
        }

        try {
            Command commandInstance = commandClass.getDeclaredConstructor().newInstance();
            commandInstance.execute(args);
        } catch (Exception e) {
            System.err.println("Error executing command '" + commandName + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printAvailableCommands() {
        System.out.println("Available commands:");
        for (String command : commandRegistry.keySet()) {
            System.out.println(" - " + command);
        }
    }
}
