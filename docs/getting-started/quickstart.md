# Quickstart

## Installation

All releases are available on our [GitHub repository](https://github.com/N1CK145/minecraft-redhook/releases/).

!!! note annotate "Looking for a specific version?"

    Browse all versions in our [releases section](https://github.com/N1CK145/minecraft-redhook/releases/)

1. Download the [latest release](https://github.com/N1CK145/minecraft-redhook/releases/latest) (.jar file)
2. Place the .jar file in your server's `plugins` directory
3. Restart or reload your server

## Configuring Actions

1. Navigate to the `plugins/Redhook` directory and open `actions.yml`
2. Add a new action configuration to your actions list:

    ```yaml linenums="1"
    actions:
        - id: player_hello                 # Unique identifier for this action
          type: PlayerMessageAction        # The action type to execute
          label: "Welcome Player"          # Display name for the action
          description:                     # Multi-line description
              - Sends a welcome message
              - to all players
        
          # Action-specific configuration
          message: "Welcome!"              # Message content
          target: null                     # Target player (null for broadcast)
    ```

3. Apply changes with `/redhook reload`

## Binding Actions to Blocks

1. Obtain the Action Binding Tool using `/redhook wand`
2. Left-click the target block to begin binding
3. Select your configured action from the menu
4. Choose the trigger condition:
    - On redstone power
    - On redstone power loss
    - Both conditions
5. Test the configuration by placing a redstone lever and toggling it
6. Your action binding is now complete and ready to use!