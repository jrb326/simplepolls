package me.jrb326.simplePolls.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import me.jrb326.simplePolls.database.PollService;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.Poll;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;

public class PollListGUI {

    @InjectLogger
    private Logger logger;

    private final PollService pollService;
    private final GuiManager guiManager;

    public PollListGUI(PollService pollService, GuiManager guiManager) {
        this.pollService = pollService;
        this.guiManager = guiManager;
    }

    public void openFor(Player player) {
        ChestGui gui = new ChestGui(6, "Active Polls");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane pane = new StaticPane(9, 6);
        gui.addPane(pane);

        List<Poll> activePolls = pollService.getActivePolls();
        logger.info("Loading {} active polls for player {}", activePolls.size(), player.getName());

        if (activePolls.isEmpty()) {
            // Show "No active polls" message
            ItemStack noPolls = new ItemStack(Material.BARRIER);
            ItemMeta meta = noPolls.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text("No Active Polls", NamedTextColor.RED));
                meta.lore(List.of(
                        Component.text("There are currently no active polls to vote on.", NamedTextColor.GRAY)));
                noPolls.setItemMeta(meta);
            }
            pane.addItem(new GuiItem(noPolls), 4, 2);
        } else {
            // Display polls
            int slot = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");

            for (Poll poll : activePolls) {
                if (slot >= 45) break; // Limit to fit in 6 rows

                ItemStack pollItem = new ItemStack(Material.WRITTEN_BOOK);
                ItemMeta meta = pollItem.getItemMeta();
                if (meta != null) {
                    meta.displayName(Component.text("Poll", NamedTextColor.AQUA));

                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(poll.getQuestion(), NamedTextColor.WHITE));
                    lore.add(Component.empty());
                    lore.add(Component.text("Created: " + poll.getCreatedAt().format(formatter), NamedTextColor.GRAY));

                    if (poll.getEndAt() != null) {
                        lore.add(Component.text("Ends: " + poll.getEndAt().format(formatter), NamedTextColor.GRAY));
                    }

                    lore.add(Component.empty());
                    lore.add(Component.text("Click to vote!", NamedTextColor.GREEN));

                    meta.lore(lore);
                    pollItem.setItemMeta(meta);
                }

                GuiItem guiItem = new GuiItem(pollItem, event -> {
                    event.setCancelled(true);
                    if (event.getWhoClicked() instanceof Player clickedPlayer) {
                        logger.info("Player {} clicked on poll {}", clickedPlayer.getName(), poll.getId());
                        // Open vote GUI
                        guiManager.openPollVote(clickedPlayer, poll.getId());
                    }
                });

                int row = slot / 9;
                int col = slot % 9;
                pane.addItem(guiItem, col, row);
                slot++;
            }
        }

        gui.show(player);
        logger.info("Opened poll list GUI for player {}", player.getName());
    }
}
