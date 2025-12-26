package com.commandline.app.commands;


public interface Command {
    void execute();
    void execute(String[] args);
}
