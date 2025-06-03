package com.example.news_aggregator.menu.main.command;

import com.example.news_aggregator.common.menu.TerminateApplicationCommand;
import com.example.news_aggregator.common.menu.impl.BaseMenuCommand;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ApplicationExitCommand
        extends BaseMenuCommand
        implements TerminateApplicationCommand {

    public ApplicationExitCommand() {
        super(StaticMenuItem.APPLICATION_EXIT_COMMAND);
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Работа завершена.");
    }
}