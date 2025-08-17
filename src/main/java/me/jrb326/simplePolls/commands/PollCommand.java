package me.jrb326.simplePolls.commands;

import javax.inject.Inject;
import me.jrb326.simplePolls.gui.GuiManager;
import me.jrb326.simplePolls.logging.InjectLogger;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.slf4j.Logger;

public class PollCommand {

    @InjectLogger
    private Logger logger;

    private final GuiManager guiManager;

    @Inject
    public PollCommand(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @Command("poll")
    @CommandDescription("Open the polls GUI to view and vote on active polls")
    @org.incendo.cloud.annotations.Permission("simplepolls.use")
    public void poll(Player player) {
        logger.info("Player {} executed /poll command", player.getName());
        guiManager.openPollList(player);
    }
}
