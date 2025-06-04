# Player Message Action

## Description
The PlayerMessageAction allows you to send chat messages to specific players or broadcast messages to all players when a redstone signal is triggered.

## Configuration

### Basic Structure

```yaml linenums="1"
actions:
    - id: player_message                    # Unique action ID
      type: PlayerMessageAction             # Type
      label: "Example Player Message"       # Friendly Name
      description:                          # Multi-line action description
        - Sends a message to a player
        - or broadcasts to all players
    
      # Action-specific configuration
      message: "Hello player!"              # The message to send
      target: STEVE                         # Target player name (null for broadcast)
```

### Configuration Options

| Option | Type | Description | Required |
|--------|------|-------------|----------|
| `id` | String | Unique identifier for the action | Yes |
| `type` | String | Must be `PlayerMessageAction` | Yes |
| `label` | String | Display name for the action | Yes |
| `description` | List | Multi-line description of the action | No |
| `message` | String | The message to send to players | Yes |
| `target` | String | Target player name. Use `null` for broadcast | No |

### Examples

#### Broadcast Message
```yaml linenums="1"
actions:
    - id: server_announcement
      type: PlayerMessageAction
      label: "Server Announcement"
      description:
          - Broadcasts a message
          - to all online players
      message: "Welcome to the server!"
      target: null
```

#### Private Message
```yaml linenums="1"
actions:
    - id: private_welcome
      type: PlayerMessageAction
      label: "Private Welcome"
      description:
          - Sends a private welcome message
          - to a specific player
      message: "Welcome back, STEVE!"
      target: "STEVE"
```

## Usage Notes

- The `target` field is optional. If not specified or set to `null`, the message will be broadcast to all players
- You can use Minecraft color codes in the message using the `&` symbol
- The message supports basic formatting like bold (`&l`), italic (`&o`), and colors (`&c` for red, etc.)
- If the target player is not online, the message will not be sent
- Messages are sent through the server's chat system, so they will appear in the chat log

## Best Practices

- Use descriptive IDs that indicate the purpose of the message
- Keep messages concise and clear
- Consider using color codes to make important messages stand out
- Test private messages with the target player online
- Use broadcast messages sparingly to avoid chat spam
