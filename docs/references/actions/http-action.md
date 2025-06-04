# HTTP Action

## Description
The HTTP Action allows you to make HTTP requests to external services when a redstone signal is triggered. This is useful for integrating with webhooks, APIs, or other web services.

## Configuration

### Basic Structure

```yaml linenums="1"
actions:
    - id: webhook_notification              # Unique action ID
      type: HttpAction                      # Type
      label: "Discord Webhook"              # Friendly Name
      description:                          # Multi-line action description
        - Sends a notification
        - to Discord channel
    
      # Action-specific configuration
      url: "https://discord.com/api/webhooks/..."  # Target URL
      method: "POST"                        # HTTP method
      headers:                              # HTTP headers
          Content-Type: "application/json"
      body: |                               # Request body
          {
              "content": "Redstone signal detected!"
          }
```

### Configuration Options

| Option | Type | Description | Required |
|--------|------|-------------|----------|
| `id` | String | Unique identifier for the action | Yes |
| `type` | String | Must be `HttpAction` | Yes |
| `label` | String | Display name for the action | Yes |
| `description` | List | Multi-line description of the action | No |
| `url` | String | The URL to send the request to | Yes |
| `method` | String | HTTP method (GET, POST, PUT, DELETE) | Yes |
| `headers` | Map | HTTP headers to include in the request | No |
| `body` | String | Request body content | No |

### Examples

#### Discord Webhook
```yaml linenums="1"
actions:
    - id: discord_alert
      type: HttpAction
      label: "Discord Alert"
      description:
          - Sends an alert
          - to Discord channel
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

#### API Request
```yaml linenums="1"
actions:
    - id: api_call
      type: HttpAction
      label: "API Request"
      description:
          - Makes an API call
          - to external service
      url: "https://api.example.com/endpoint"
      method: "GET"
      headers:
          Authorization: "Bearer your-token"
          Accept: "application/json"
```

## Usage Notes

- The `url` must be a valid HTTP/HTTPS URL
- Supported HTTP methods are: GET, POST, PUT, DELETE
- For POST/PUT requests, you can include a request body
- Headers are optional but often required for authentication
- The action will not retry failed requests
- Long-running requests may impact server performance
- HTTPS URLs are recommended for security

## Best Practices

- Use descriptive IDs that indicate the purpose of the request
- Keep request bodies concise and well-formatted
- Use appropriate HTTP methods for your use case
- Test the endpoint before deploying
- Use HTTPS for all external requests
- Consider rate limiting for frequent triggers
