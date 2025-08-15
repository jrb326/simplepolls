package me.jrb326.simplePolls.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.jrb326.simplePolls.database.PollService;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.Poll;
import me.jrb326.simplePolls.model.PollOption;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;

public class PollVoteGUI {

    @InjectLogger
    private Logger logger;

    private final PollService pollService;
    private final GuiManager guiManager;

    public PollVoteGUI(PollService pollService, GuiManager guiManager) {
        this.pollService = pollService;
        this.guiManager = guiManager;
    }

    public void openFor(Player player, String pollId) {
        Optional<Poll> pollOpt = pollService.getPoll(pollId);
        if (pollOpt.isEmpty()) {
            player.sendMessage(Component.text("Poll not found!", NamedTextColor.RED));
            return;
        }

        Poll poll = pollOpt.get();
        if (!poll.isActive()) {
            player.sendMessage(Component.text("This poll is no longer active!", NamedTextColor.RED));
            return;
        }

        String playerId = player.getUniqueId().toString();
        boolean hasVoted = pollService.hasPlayerVoted(pollId, playerId);

        ChestGui gui = new ChestGui(
                3,
                "Vote: "
                        + poll.getQuestion()
                                .substring(0, Math.min(poll.getQuestion().length(), 20)) + "...");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane pane = new StaticPane(9, 3);
        gui.addPane(pane);

        // Add back button
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(Component.text("« Back to Polls", NamedTextColor.YELLOW));
            backButton.setItemMeta(backMeta);
        }

        GuiItem backItem = new GuiItem(backButton, event -> {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player clickedPlayer) {
                guiManager.openPollList(clickedPlayer);
            }
        });
        pane.addItem(backItem, 0, 0);

        // Display poll question
        ItemStack questionItem = new ItemStack(Material.PAPER);
        ItemMeta questionMeta = questionItem.getItemMeta();
        if (questionMeta != null) {
            questionMeta.displayName(Component.text("Poll Question", NamedTextColor.AQUA));
            questionMeta.lore(List.of(Component.text(poll.getQuestion(), NamedTextColor.WHITE)));
            questionItem.setItemMeta(questionMeta);
        }
        pane.addItem(new GuiItem(questionItem), 4, 0);

        // Get poll options
        List<PollOption> options = pollService.getPollOptions(pollId);
        logger.info("Loading {} options for poll {} for player {}", options.size(), pollId, player.getName());

        if (hasVoted) {
            // Show voting results with current vote highlighted
            Optional<me.jrb326.simplePolls.model.Vote> playerVoteOpt = pollService.getPlayerVote(pollId, playerId);
            String votedOptionId = playerVoteOpt
                    .map(me.jrb326.simplePolls.model.Vote::getOptionId)
                    .orElse("");

            int startCol = Math.max(0, (9 - options.size()) / 2); // Center the options

            for (int i = 0; i < options.size(); i++) {
                PollOption option = options.get(i);
                int voteCount = pollService.getVoteCount(option.getId());
                boolean isPlayerVote = option.getId().equals(votedOptionId);

                ItemStack optionItem = new ItemStack(isPlayerVote ? Material.EMERALD : option.getIcon());
                ItemMeta optionMeta = optionItem.getItemMeta();
                if (optionMeta != null) {
                    optionMeta.displayName(Component.text(
                            option.getOptionText(), isPlayerVote ? NamedTextColor.GREEN : NamedTextColor.WHITE));

                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text("Votes: " + voteCount, NamedTextColor.GOLD));
                    if (isPlayerVote) {
                        lore.add(Component.text("✓ Your vote", NamedTextColor.GREEN));
                    }
                    lore.add(Component.text("You have already voted!", NamedTextColor.RED));

                    optionMeta.lore(lore);
                    optionItem.setItemMeta(optionMeta);
                }

                pane.addItem(new GuiItem(optionItem), startCol + i, 1);
            }
        } else {
            // Show voting options
            int startCol = Math.max(0, (9 - options.size()) / 2); // Center the options

            for (int i = 0; i < options.size(); i++) {
                PollOption option = options.get(i);
                int voteCount = pollService.getVoteCount(option.getId());

                ItemStack optionItem = new ItemStack(option.getIcon());
                ItemMeta optionMeta = optionItem.getItemMeta();
                if (optionMeta != null) {
                    optionMeta.displayName(Component.text(option.getOptionText(), NamedTextColor.WHITE));

                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text("Current votes: " + voteCount, NamedTextColor.GOLD));
                    lore.add(Component.empty());
                    lore.add(Component.text("Click to vote!", NamedTextColor.GREEN));

                    optionMeta.lore(lore);
                    optionItem.setItemMeta(optionMeta);
                }

                GuiItem optionGuiItem = new GuiItem(optionItem, event -> {
                    event.setCancelled(true);
                    if (event.getWhoClicked() instanceof Player clickedPlayer) {
                        String clickedPlayerId = clickedPlayer.getUniqueId().toString();

                        // Double-check they haven't voted since opening the GUI
                        if (pollService.hasPlayerVoted(pollId, clickedPlayerId)) {
                            clickedPlayer.sendMessage(
                                    Component.text("You have already voted in this poll!", NamedTextColor.RED));
                            openFor(clickedPlayer, pollId); // Refresh the GUI
                            return;
                        }

                        // Cast the vote
                        if (pollService.castVote(pollId, option.getId(), clickedPlayerId)) {
                            clickedPlayer.sendMessage(Component.text("Vote cast successfully!", NamedTextColor.GREEN));
                            logger.info(
                                    "Player {} successfully voted for option {} in poll {}",
                                    clickedPlayer.getName(),
                                    option.getId(),
                                    pollId);
                            openFor(clickedPlayer, pollId); // Refresh to show results
                        } else {
                            clickedPlayer.sendMessage(
                                    Component.text("Failed to cast vote. Please try again.", NamedTextColor.RED));
                        }
                    }
                });

                pane.addItem(optionGuiItem, startCol + i, 1);
            }
        }

        gui.show(player);
        logger.info("Opened vote GUI for poll {} for player {} (hasVoted: {})", pollId, player.getName(), hasVoted);
    }
}
