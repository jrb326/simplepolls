# SimplePolls - CreatePoll Command

## Overview
The `/createpoll` command provides an interactive GUI for creating polls in Minecraft.

## Usage

### Command
```
/createpoll
```

**Permission:** `simplepolls.createpoll` (default: op)

### Interactive GUI Workflow

1. **Execute the Command**: Use `/createpoll` to open the interactive GUI
2. **Set Question**: Click the "Set Poll Question" book to enter question via chat
3. **Add Options**: Click option slots (up to 6) to add poll options via chat
4. **Create Poll**: Once requirements are met, click "Create Poll" button
5. **Cancel**: Click "Cancel" to abort poll creation

### Requirements
- Question must be set
- At least 2 options required (maximum 6)
- All options must have text

### GUI Layout
```
[?] [Book] [?] [?] [?] [?] [?] [?] [?]
[?] [?] [?] [Opt1] [Opt2] [Opt3] [?] [?] [?]
[?] [?] [?] [Opt4] [Opt5] [Opt6] [?] [?] [?]
[?] [?] [?] [?] [?] [?] [?] [?] [?]
[?] [?] [?] [?] [?] [?] [?] [?] [?]
[X] [?] [?] [?] [Create] [?] [?] [?] [?]
```

- **Book**: Set poll question
- **Opt1-6**: Set poll options (1-6)
- **Create**: Create the poll (green if valid, red if invalid)
- **X**: Cancel poll creation

### Example Usage Flow
1. Player runs `/createpoll`
2. Clicks book icon → Types in chat: "What's your favorite color?"
3. Clicks Option 1 → Types in chat: "Red"
4. Clicks Option 2 → Types in chat: "Blue"
5. Clicks "Create Poll" → Poll is created successfully

## Architecture

The implementation follows clean architecture principles:

- **Command Layer**: `CreatePollCommand` handles the `/createpoll` command
- **GUI Layer**: `CreatePollGui` manages the interactive interface
- **Input Layer**: `ChatInputHandler` captures chat input during poll creation
- **Service Layer**: `PollService` contains business logic and validation
- **Model Layer**: `Poll` and `PollOption` represent data structures
- **DI Layer**: `CommandModule` wires dependencies with Guice

## Database Integration

The current implementation logs poll creation but doesn't persist to database. The database schema is already prepared in `V1__init.sql` for future integration.

## Dependencies Used

- **Paper API**: Minecraft server platform
- **cloud-paper**: Command framework with annotations
- **InventoryFramework**: GUI creation library
- **Guice**: Dependency injection
- **Adventure**: Text components and formatting