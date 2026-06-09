package com.tigrbank.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public Command get(String name) {
        return commands.get(name);
    }

    public void execute(String name) {
        Command cmd = commands.get(name);
        if (cmd != null) {
            cmd.execute();
        } else {
            System.out.println("Unknown command: " + name);
        }
    }

    public Map<String, Command> getAll() {
        return commands;
    }
}