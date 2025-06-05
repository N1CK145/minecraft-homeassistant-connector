# Running the Server with Docker

This guide will help you set up and run the Minecraft server using Docker.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed on your system
- [Docker Compose](https://docs.docker.com/compose/install/) installed on your system

## Quick Start

1. Navigate to the `docker` directory in the project root:
   ```bash
   cd docker
   ```

2. Start the server:
   ```bash
   docker-compose up -d
   ```

The server will start in detached mode. You can connect to it using the default Minecraft port (25565).

## Server Configuration

The server is configured using environment variables in the `docker-compose.yml` file. Here are the default settings:

- **Server Type**: Paper
- **Version**: 1.21.4
- **Memory**: 2GB
- **Difficulty**: Normal
- **MOTD**: "RedHook Development Server"
- **RCON**: Enabled (Port: 25575, Password: minecraft)

### Customizing Configuration

To modify these settings, edit the `docker-compose.yml` file. Here are some common customizations:

```yaml
environment:
  MEMORY: "4G"              # Increase memory allocation
  DIFFICULTY: "hard"        # Change difficulty
  MOTD: "My Custom Server"  # Change server message
```

## Managing the Server

### Viewing Logs

To view server logs:
```bash
docker-compose logs -f
```

### Stopping the Server

To stop the server:
```bash
docker-compose down
```

### Restarting the Server

To restart the server:
```bash
docker-compose restart
```

## Data Persistence

Server data is stored in the `docker/data` directory, which is mounted as a volume. This means:
- World data persists between restarts
- Server configuration files are preserved
- You can backup the `data` directory to save your server state

## RCON Access

The server has RCON enabled for remote administration:
- Port: 25575
- Password: minecraft

You can use any RCON client to connect to the server for administrative tasks.

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   - Ensure no other Minecraft server is running on port 25565
   - Check if the port is available: `netstat -an | findstr 25565`

2. **Server Not Starting**
   - Check Docker logs: `docker-compose logs`
   - Verify Docker is running: `docker ps`

3. **Memory Issues**
   - Adjust the `MEMORY` environment variable in `docker-compose.yml`
   - Ensure your system has enough available RAM

### Getting Help

If you encounter any issues not covered here:
1. Check the [itzg/minecraft-server documentation](https://github.com/itzg/docker-minecraft-server)
2. Review the server logs for specific error messages
3. Open an issue in the project repository 