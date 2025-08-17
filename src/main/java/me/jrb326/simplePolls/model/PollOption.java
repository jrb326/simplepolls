package me.jrb326.simplePolls.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollOption {
    private String id;
    private String pollId;
    private String optionText;
    private int sortOrder;
    private Material iconMaterial;
    private Integer customModelData;

    public Material getIcon() {
        if (iconMaterial != null) {
            return iconMaterial;
        }
        return Material.PAPER; // Default icon
    }
}
