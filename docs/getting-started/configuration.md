# Configuration

Redhook uses two main configuration files to manage actions and their bindings to redstone blocks:

- `actions.yml`: Defines the available actions and their configurations
- `bindings.yml`: Stores the bindings between redstone blocks and actions

## Actions Configuration

The `actions.yml` file is located in your server's `plugins/Redhook` directory. This file defines all available actions that can be triggered by redstone signals.

### Basic Structure

```yaml linenums="1"
actions:
    - id: unique_action_id          # Unique identifier for this action
      type: ActionType              # The type of action to execute
      label: "Friendly Name"        # Display name for the action
      description:                  # Multi-line description
          - First line
          - Second line
      
      # Action-specific configuration
      # These fields vary based on the action type
```

### Available Action Types

!!! note annotate "Looking for a specific action?"
    You can find all actions [here](../references/actions/player-message-action.md)

#### Player Message Action

Sends a chat message to one or more players.

```yaml linenums="1"
actions:
    - id: player_hello
      type: PlayerMessageAction
      label: "Welcome Message"
      description:
          - Sends a welcome message
          - to all players
      
      # Action-specific configuration
      message: "Welcome to the server!"    # The message to send
      target: null                         # Target player name (null for broadcast)
```

#### HTTP Action

Makes HTTP requests to external services.

```yaml linenums="1"
actions:
    - id: webhook_notification
      type: HttpAction
      label: "Discord Webhook"
      description:
          - Sends a notification
          - to Discord channel
      
      # Action-specific configuration
      url: "https://discord.com/api/webhooks/..."
      method: "POST"
      headers:
          Content-Type: "application/json"
      body: |
          {
              "content": "Redstone signal detected!"
          }
```

## Bindings Configuration

The `bindings.yml` file stores the relationships between redstone blocks and actions. This file is automatically managed by the plugin and should not be edited manually.

### Structure

```yaml linenums="1"
bindings:
    - world: "world"                # World name
      x: 100                        # X coordinate
      y: 64                         # Y coordinate
      z: 200                        # Z coordinate
      instances:                    # List of actions bound to this block
          - actionId: "player_hello"
            triggerCondition: "POWER_ON"  # POWER_ON, POWER_OFF, or BOTH
```

## Managing Configuration

### Reloading Configuration

To apply changes to your configuration files:

1. Edit the `actions.yml` file
2. Run the command `/redhook reload`

### Creating New Actions

1. Open `actions.yml` in your server's `plugins/Redhook` directory
2. Add a new action configuration following the structure above
3. Save the file and run `/redhook reload`

### Binding Actions to Blocks

1. Obtain the Action Binding Tool using `/redhook wand`
2. Left-click the target block to begin binding
3. Select your configured action from the menu
4. Choose the trigger condition:
    - On redstone power
    - On redstone power loss
    - Both conditions
5. Test the configuration by placing a redstone lever and toggling it

## Best Practices

- Use descriptive IDs for your actions
- Keep action labels clear and concise
- Document complex actions with detailed descriptions
- Test new configurations in a safe environment
- Back up your configuration files before making major changes