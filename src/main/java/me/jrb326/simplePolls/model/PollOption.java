package me.jrb326.simplePolls.model;

import lombok.Builder;
import lombok.Value;
import org.bukkit.Material;

@Value
@Builder
public class PollOption {
    String id;
    String pollId;
    String optionText;
    int sortOrder;
    String iconMaterial;
    Integer iconCustomModelData;

    public Material getIcon() {
        if (iconMaterial != null) {
            try {
                return Material.valueOf(iconMaterial);
            } catch (IllegalArgumentException e) {
                // Fall back to default if invalid material
            }
        }
        return Material.PAPER; // Default icon
    }
}
