package me.jrb326.simplePolls.gui;

import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.PollOption;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ChatInputHandler implements Listener {

    @InjectLogger
    private Logger logger;

    private final CreatePollGui createPollGui;
    
    // Track what input we're waiting for from each player
    private final Map<Player, InputType> waitingForInput = new HashMap<>();
    private final Map<Player, Integer> waitingForOptionIndex = new HashMap<>();

    @Inject
    public ChatInputHandler(CreatePollGui createPollGui) {
        this.createPollGui = createPollGui;
    }

    public void waitForQuestion(Player player) {
        waitingForInput.put(player, InputType.QUESTION);
        logger.debug("Waiting for question input from player {}", player.getName());
    }

    public void waitForOption(Player player, int optionIndex) {
        waitingForInput.put(player, InputType.OPTION);
        waitingForOptionIndex.put(player, optionIndex);
        logger.debug("Waiting for option {} input from player {}", optionIndex, player.getName());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        
        if (!waitingForInput.containsKey(player)) {
            return; // Not waiting for input from this player
        }
        
        event.setCancelled(true); // Cancel the chat message
        
        String input = event.getMessage().trim();
        InputType inputType = waitingForInput.remove(player);
        
        if (input.isEmpty()) {
            player.sendMessage(Component.text("Input cannot be empty. Please try again.", NamedTextColor.RED));
            return;
        }
        
        switch (inputType) {
            case QUESTION:
                handleQuestionInput(player, input);
                break;
            case OPTION:
                Integer optionIndex = waitingForOptionIndex.remove(player);
                if (optionIndex != null) {
                    handleOptionInput(player, input, optionIndex);
                }
                break;
        }
    }

    private void handleQuestionInput(Player player, String question) {
        logger.debug("Received question input from {}: {}", player.getName(), question);
        
        // Store the question in the GUI's creation data
        createPollGui.setQuestion(player, question);
        
        player.sendMessage(Component.text("Question set: " + question, NamedTextColor.GREEN));
        
        // Reopen the GUI
        createPollGui.openGui(player);
    }

    private void handleOptionInput(Player player, String optionText, int optionIndex) {
        logger.debug("Received option {} input from {}: {}", optionIndex, player.getName(), optionText);
        
        PollOption option = PollOption.builder()
            .optionText(optionText)
            .iconMaterial(org.bukkit.Material.PAPER) // Default icon
            .build();
        
        // Store the option in the GUI's creation data
        createPollGui.setOption(player, optionIndex, option);
        
        player.sendMessage(Component.text("Option " + (optionIndex + 1) + " set: " + optionText, NamedTextColor.GREEN));
        
        // Reopen the GUI
        createPollGui.openGui(player);
    }

    public void cancelInput(Player player) {
        waitingForInput.remove(player);
        waitingForOptionIndex.remove(player);
    }

    private enum InputType {
        QUESTION,
        OPTION
    }
}