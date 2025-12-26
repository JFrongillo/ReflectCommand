package com.commandline.app.commands.api;


public interface Command {
    void execute();
    void execute(String[] args);
}
