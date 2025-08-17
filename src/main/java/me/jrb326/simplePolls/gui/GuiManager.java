package me.jrb326.simplePolls.gui;

import javax.inject.Inject;
import me.jrb326.simplePolls.database.PollService;
import org.bukkit.entity.Player;

public class GuiManager {

    private final PollService pollService;

    @Inject
    public GuiManager(PollService pollService) {
        this.pollService = pollService;
    }

    public void openPollList(Player player) {
        PollListGUI pollListGUI = new PollListGUI(pollService, this);
        pollListGUI.openFor(player);
    }

    public void openPollVote(Player player, String pollId) {
        PollVoteGUI pollVoteGUI = new PollVoteGUI(pollService, this);
        pollVoteGUI.openFor(player, pollId);
    }
}
