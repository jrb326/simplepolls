package me.jrb326.simplePolls.service;

import me.jrb326.simplePolls.model.PollOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PollServiceTest {

    private PollService pollService;

    @BeforeEach
    void setUp() {
        pollService = new PollService();
    }

    @Test
    void testCreatePoll_ValidInput() {
        // Arrange
        String question = "What is your favorite color?";
        List<PollOption> options = Arrays.asList(
            createOption("Red"),
            createOption("Blue"),
            createOption("Green")
        );
        UUID createdBy = UUID.randomUUID();

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> pollService.createPoll(question, options, createdBy));
    }

    @Test
    void testIsValidPollData_ValidData() {
        // Arrange
        String question = "What is your favorite color?";
        List<PollOption> options = Arrays.asList(
            createOption("Red"),
            createOption("Blue")
        );

        // Act
        boolean result = pollService.isValidPollData(question, options);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsValidPollData_EmptyQuestion() {
        // Arrange
        String question = "";
        List<PollOption> options = Arrays.asList(
            createOption("Red"),
            createOption("Blue")
        );

        // Act
        boolean result = pollService.isValidPollData(question, options);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsValidPollData_NullQuestion() {
        // Arrange
        String question = null;
        List<PollOption> options = Arrays.asList(
            createOption("Red"),
            createOption("Blue")
        );

        // Act
        boolean result = pollService.isValidPollData(question, options);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsValidPollData_TooFewOptions() {
        // Arrange
        String question = "What is your favorite color?";
        List<PollOption> options = Collections.singletonList(createOption("Red"));

        // Act
        boolean result = pollService.isValidPollData(question, options);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsValidPollData_TooManyOptions() {
        // Arrange
        String question = "What is your favorite color?";
        List<PollOption> options = Arrays.asList(
            createOption("Red"),
            createOption("Blue"),
            createOption("Green"),
            createOption("Yellow"),
            createOption("Purple"),
            createOption("Orange"),
            createOption("Pink") // 7 options - too many
        );

        // Act
        boolean result = pollService.isValidPollData(question, options);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsValidPollData_EmptyOptionText() {
        // Arrange
        String question = "What is your favorite color?";
        List<PollOption> options = Arrays.asList(
            createOption("Red"),
            createOption("") // Empty option text
        );

        // Act
        boolean result = pollService.isValidPollData(question, options);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsValidPollData_SixOptionsExactly() {
        // Arrange
        String question = "What is your favorite color?";
        List<PollOption> options = Arrays.asList(
            createOption("Red"),
            createOption("Blue"),
            createOption("Green"),
            createOption("Yellow"),
            createOption("Purple"),
            createOption("Orange")
        );

        // Act
        boolean result = pollService.isValidPollData(question, options);

        // Assert
        assertTrue(result);
    }

    private PollOption createOption(String text) {
        return PollOption.builder()
            .optionText(text)
            .iconMaterial(Material.PAPER)
            .build();
    }
}