package me.jrb326.simplePolls.commands;

import me.jrb326.simplePolls.gui.CreatePollGui;
import me.jrb326.simplePolls.logging.InjectLogger;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreatePollCommand {

    @InjectLogger
    private Logger logger;

    private final CreatePollGui createPollGui;

    @Inject
    public CreatePollCommand(CreatePollGui createPollGui) {
        this.createPollGui = createPollGui;
    }

    @Command("createpoll")
    @Permission("simplepolls.createpoll")
    public void createPoll(Player player) {
        logger.info("Player {} is creating a new poll", player.getName());
        createPollGui.openGui(player);
    }
}