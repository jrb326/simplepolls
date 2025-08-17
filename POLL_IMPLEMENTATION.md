# SimplePolls - /poll Command Implementation

This implementation adds a `/poll` command that opens an interactive GUI for players to view and vote on active polls.

## Features

### `/poll` Command
- Opens a GUI listing all active polls
- Players can click on polls to view details and vote
- Each poll displays its question and creation date
- Supports voting on poll options through an interactive GUI

### Vote Management
- Each player can only vote once per poll
- Votes are stored in the database with duplicate prevention
- Players can see vote counts and their own votes
- Real-time vote counting and display

### GUI Interface
- **Poll List GUI**: Shows all active polls as clickable books
- **Poll Vote GUI**: Shows poll details and voting options
- **Navigation**: Easy navigation between GUIs with back buttons
- **Visual Feedback**: Different icons and colors for voted/unvoted options

## Setup

### Database Configuration
1. Create a MySQL database for the plugin
2. Configure database settings in `config.yml`:
```yaml
database:
  url: "jdbc:mysql://localhost:3306/simplepolls"
  username: "your_username"
  password: "your_password"
  pool:
    maximumPoolSize: 10
    minimumIdle: 2
```

### Test Data
To test the implementation, you can load sample polls using the test data:
```sql
-- Execute the contents of src/main/resources/db/test_data.sql
-- This creates 3 sample polls with various options
```

### Permissions
- `simplepolls.use` - Access to view and vote on polls (default: true)

## Usage

### For Players
1. Execute `/poll` to open the poll list GUI
2. Click on any poll book to view the poll details
3. Click on an option to vote (if you haven't voted yet)
4. Use the back arrow to return to the poll list
5. View vote counts and your own votes

### GUI Behavior
- **Active Polls Only**: Only shows polls that are open and not expired
- **Vote Prevention**: Players cannot vote twice on the same poll
- **Visual Indicators**: Voted options show as emerald blocks with checkmarks
- **Vote Counts**: Shows current vote counts for all options

## Technical Implementation

### Architecture
- **Database Layer**: JDBI with DAO pattern for data access
- **Service Layer**: PollService handles business logic and error handling
- **GUI Layer**: Inventory Framework for interactive GUIs
- **Command Layer**: Cloud annotations for command handling
- **Dependency Injection**: Guice for clean dependency management

### Database Schema
Uses the existing schema with three tables:
- `polls` - Poll definitions with active status checking
- `poll_options` - Up to 6 options per poll with custom icons
- `votes` - Player votes with duplicate prevention

### Key Classes
- `PollCommand` - Handles `/poll` command execution
- `PollListGUI` - Main poll listing interface
- `PollVoteGUI` - Individual poll voting interface
- `PollService` - Database operations and business logic
- `GuiManager` - Navigation between GUIs

## Error Handling
- Database connection failures are logged and handled gracefully
- Invalid polls show error messages to players
- Duplicate vote attempts are prevented and logged
- GUI errors don't crash the plugin

## Testing
1. Load the test data using `test_data.sql`
2. Grant yourself the `simplepolls.use` permission
3. Run `/poll` to test the GUI functionality
4. Try voting on different polls to test deduplication
5. Check logs for any errors or issues

The implementation fully satisfies the requirements:
- ✅ `/poll` command opens GUI with active polls
- ✅ Shows poll question and creation date
- ✅ Clickable polls open voting GUI
- ✅ Players can vote once per poll
- ✅ Votes stored in database with duplicate prevention
- ✅ Vote counting and display