# Minecraft Redhook

Bring Redstone to the Real World! Redhook is a powerful Minecraft plugin that allows you to connect redstone signals to real-world actions through webhooks and API integrations.

## 🚀 Features

- **Webhook Integration**: Connect your redstone contraptions to Discord, Slack, or any web service
- **Player Messages**: Send messages to specific players or broadcast to all
- **Easy Configuration**: Simple YAML-based configuration
- **Visual Binding Tool**: Intuitive in-game tool for binding actions to blocks
- **Multiple Trigger Conditions**: Trigger on power, power loss, or both
- **Extensible**: Create custom actions through the plugin API

## 🎮 Quick Start

1. Download the latest release from our [GitHub releases page](https://github.com/N1CK145/minecraft-redhook/releases)
2. Place the JAR file in your server's `plugins` directory
3. Restart your server
4. Use `/redhook wand` to get started

## 📝 Example Configurations

### Discord Integration

```yaml linenums="1"
actions:
    - id: discord_alert
      type: HttpAction
      label: "Discord Alert"
      description:
          - Sends an alert to Discord
          - when redstone is triggered
      url: "https://discord.com/api/webhooks/your-webhook-url"
      method: "POST"
      headers:
          Content-Type: "application/json"
      body: |
          {
              "content": "Redstone signal detected!",
              "embeds": [{
                  "title": "Redstone Alert",
                  "description": "A redstone signal was triggered",
                  "color": 16711680
              }]
          }
```

### Player Notifications

```yaml linenums="1"
actions:
    - id: welcome_message
      type: PlayerMessageAction
      label: "Welcome Message"
      description:
          - Sends a welcome message
          - to all players
      message: "&aWelcome to the server!"
      target: null
```

## 🎯 Use Cases

- **Security Systems**: Alert Discord when someone triggers a security system
- **Welcome Messages**: Send personalized messages when players enter specific areas
- **Server Monitoring**: Monitor redstone contraptions through webhook notifications
- **Integration with External Services**: Connect your Minecraft world to other applications
- **Automated Notifications**: Get notified about important events in your world

## 🔧 Configuration

Redhook uses two main configuration files:

- `actions.yml`: Define your actions and their configurations
- `bindings.yml`: Store the relationships between redstone blocks and actions

See our [Configuration Guide](getting-started/configuration.md) for detailed information.

## 🛠️ Developer API

Want to create custom actions? Check out our [Developer Documentation](developer-docs/getting-started.md) to learn how to extend Redhook with your own actions.

## 📚 Documentation

- [Getting Started](getting-started/quickstart.md)
- [Configuration Guide](getting-started/configuration.md)
- [Action Reference](references/actions/http-action.md)
- [Developer Guide](developer-docs/getting-started.md)

## 🤝 Contributing

We welcome contributions! Check out our [GitHub repository](https://github.com/N1CK145/minecraft-redhook) to:

- Report bugs
- Suggest features
- Submit pull requests
- Improve documentation

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/N1CK145/minecraft-redhook/blob/main/LICENSE) file for details.