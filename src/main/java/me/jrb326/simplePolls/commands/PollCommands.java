package me.jrb326.simplePolls.commands;

import java.util.Optional;
import javax.inject.Inject;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.Poll;
import me.jrb326.simplePolls.service.PollService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.slf4j.Logger;

public class PollCommands {

    @InjectLogger
    private Logger logger;

    private final PollService pollService;

    @Inject
    public PollCommands(PollService pollService) {
        this.pollService = pollService;
    }

    @Command("poll close <pollId> [reason]")
    @Permission("simplepolls.staff.close")
    public void closePoll(
            CommandSender sender,
            @Argument("pollId") String pollId,
            @Argument(
                            value = "reason",
                            suggestions = {"Completed", "Invalid", "Duplicate"})
                    String reason) {

        // Validate sender is a player for UUID access
        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    Component.text("This command can only be used by players.").color(NamedTextColor.RED));
            return;
        }

        // Check if poll exists and is open
        Optional<Poll> pollOpt = pollService.getPoll(pollId);
        if (pollOpt.isEmpty()) {
            sender.sendMessage(
                    Component.text("Poll with ID '" + pollId + "' not found.").color(NamedTextColor.RED));
            return;
        }

        Poll poll = pollOpt.get();
        if (!poll.isOpen()) {
            sender.sendMessage(
                    Component.text("Poll '" + pollId + "' is already closed.").color(NamedTextColor.YELLOW));
            return;
        }

        // Close the poll
        String closeReason = reason != null ? reason : "Closed by staff";
        boolean success = pollService.closePoll(pollId, player.getUniqueId(), closeReason);

        if (success) {
            sender.sendMessage(
                    Component.text("Successfully closed poll '" + pollId + "'").color(NamedTextColor.GREEN));
            logger.info("Poll {} closed by {} ({})", pollId, player.getName(), player.getUniqueId());
        } else {
            sender.sendMessage(
                    Component.text("Failed to close poll '" + pollId + "'").color(NamedTextColor.RED));
        }
    }

    @Command("poll remove <pollId>")
    @Permission("simplepolls.staff.remove")
    public void removePoll(CommandSender sender, @Argument("pollId") String pollId) {

        // Validate sender is a player for UUID access
        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    Component.text("This command can only be used by players.").color(NamedTextColor.RED));
            return;
        }

        // Check if poll exists
        Optional<Poll> pollOpt = pollService.getPoll(pollId);
        if (pollOpt.isEmpty()) {
            sender.sendMessage(
                    Component.text("Poll with ID '" + pollId + "' not found.").color(NamedTextColor.RED));
            return;
        }

        // Remove the poll
        boolean success = pollService.removePoll(pollId);

        if (success) {
            sender.sendMessage(
                    Component.text("Successfully removed poll '" + pollId + "'").color(NamedTextColor.GREEN));
            logger.info("Poll {} removed by {} ({})", pollId, player.getName(), player.getUniqueId());
        } else {
            sender.sendMessage(
                    Component.text("Failed to remove poll '" + pollId + "'").color(NamedTextColor.RED));
        }
    }
}
