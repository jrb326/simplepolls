package me.jrb326.simplePolls.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.PollOption;
import me.jrb326.simplePolls.service.PollService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class CreatePollGui {

    @InjectLogger
    private Logger logger;

    private final PollService pollService;
    private final ChatInputHandler chatInputHandler;
    
    // Store poll creation data per player
    private final Map<Player, PollCreationData> creationData = new HashMap<>();

    @Inject
    public CreatePollGui(PollService pollService, ChatInputHandler chatInputHandler) {
        this.pollService = pollService;
        this.chatInputHandler = chatInputHandler;
    }

    public void openGui(Player player) {
        logger.debug("Opening poll creation GUI for player {}", player.getName());
        
        // Initialize or get existing creation data
        PollCreationData data = creationData.computeIfAbsent(player, k -> new PollCreationData());
        
        ChestGui gui = ChestGui.builder()
            .title(Component.text("Create Poll", NamedTextColor.DARK_GREEN))
            .rows(6)
            .build();

        StaticPane pane = new StaticPane(0, 0, 9, 6);
        gui.addPane(pane);

        // Question setup item
        ItemStack questionItem = createItem(Material.BOOK, 
            "Set Poll Question", 
            List.of("Click to set the poll question",
                   data.question != null ? "Current: " + data.question : "Not set"));
        pane.addItem(new GuiItem(questionItem, event -> {
            event.setCancelled(true);
            player.closeInventory();
            player.sendMessage(Component.text("Type your poll question in chat:", NamedTextColor.YELLOW));
            chatInputHandler.waitForQuestion(player);
        }), 1, 1);

        // Options setup (up to 6 options)
        for (int i = 0; i < 6; i++) {
            final int optionIndex = i;
            Material material = i < data.options.size() ? Material.PAPER : Material.GRAY_STAINED_GLASS_PANE;
            String name = "Option " + (i + 1);
            List<String> lore = new ArrayList<>();
            
            if (i < data.options.size()) {
                PollOption option = data.options.get(i);
                lore.add("Current: " + option.getOptionText());
                lore.add("Click to edit");
            } else {
                lore.add("Click to add option");
                lore.add("(Empty)");
            }
            
            ItemStack optionItem = createItem(material, name, lore);
            pane.addItem(new GuiItem(optionItem, event -> {
                event.setCancelled(true);
                player.closeInventory();
                player.sendMessage(Component.text("Type option " + (optionIndex + 1) + " text in chat:", NamedTextColor.YELLOW));
                chatInputHandler.waitForOption(player, optionIndex);
            }), (i % 3) + 3, 2 + (i / 3));
        }

        // Create poll button
        Material createMaterial = data.isValid() ? Material.LIME_CONCRETE : Material.RED_CONCRETE;
        String createName = data.isValid() ? "Create Poll" : "Cannot Create Poll";
        List<String> createLore = new ArrayList<>();
        if (data.isValid()) {
            createLore.add("Click to create the poll!");
        } else {
            createLore.add("Requirements:");
            createLore.add("- Set a question");
            createLore.add("- Add at least 2 options");
        }
        
        ItemStack createItem = createItem(createMaterial, createName, createLore);
        pane.addItem(new GuiItem(createItem, event -> {
            event.setCancelled(true);
            if (data.isValid()) {
                // Create the poll with only valid options
                List<PollOption> validOptions = data.getValidOptions();
                pollService.createPoll(data.question, validOptions, player.getUniqueId());
                player.closeInventory();
                player.sendMessage(Component.text("Poll created successfully!", NamedTextColor.GREEN));
                creationData.remove(player); // Clean up
                chatInputHandler.cancelInput(player); // Clean up chat handler
            } else {
                player.sendMessage(Component.text("Please complete all requirements first!", NamedTextColor.RED));
            }
        }), 4, 5);

        // Cancel button
        ItemStack cancelItem = createItem(Material.BARRIER, "Cancel", List.of("Click to cancel poll creation"));
        pane.addItem(new GuiItem(cancelItem, event -> {
            event.setCancelled(true);
            player.closeInventory();
            creationData.remove(player); // Clean up
            chatInputHandler.cancelInput(player); // Clean up chat handler
            player.sendMessage(Component.text("Poll creation cancelled.", NamedTextColor.GRAY));
        }), 0, 5);

        gui.show(player);
    }

    public void setQuestion(Player player, String question) {
        PollCreationData data = creationData.computeIfAbsent(player, k -> new PollCreationData());
        data.question = question;
        logger.debug("Set question for player {}: {}", player.getName(), question);
    }

    public void setOption(Player player, int optionIndex, PollOption option) {
        PollCreationData data = creationData.computeIfAbsent(player, k -> new PollCreationData());
        
        // Ensure the options list is large enough
        while (data.options.size() <= optionIndex) {
            data.options.add(null);
        }
        
        data.options.set(optionIndex, option);
        logger.debug("Set option {} for player {}: {}", optionIndex, player.getName(), option.getOptionText());
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            List<Component> loreComponents = lore.stream()
                .map(line -> Component.text(line, NamedTextColor.GRAY))
                .toList();
            meta.lore(loreComponents);
            item.setItemMeta(meta);
        }
        return item;
    }

    // Helper class to store poll creation data per player
    private static class PollCreationData {
        String question;
        List<PollOption> options = new ArrayList<>();

        boolean isValid() {
            return question != null && !question.trim().isEmpty() && 
                   getValidOptions().size() >= 2 && getValidOptions().size() <= 6;
        }
        
        private List<PollOption> getValidOptions() {
            return options.stream()
                .filter(opt -> opt != null && opt.getOptionText() != null && !opt.getOptionText().trim().isEmpty())
                .toList();
        }
    }
}