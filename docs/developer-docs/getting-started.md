# Getting Started

This guide will help you set up your development environment and start contributing to the Redhook plugin.

## Prerequisites

- Java Development Kit (JDK) 21 or later
- Gradle 8.8 or later
- An IDE (IntelliJ IDEA recommended)
- Git

## Setting Up the Development Environment

1. Clone the repository:
    ```bash
    git clone https://github.com/N1CK145/minecraft-redhook.git
    cd minecraft-redhook
    ```

2. Open the project in your IDE:
    - For IntelliJ IDEA: Open the project directory and select "Open as Project"
    - The IDE should automatically detect the Gradle project and set it up

3. Build the project:
    ```bash
    ./gradlew build
    ```

## Project Structure

```
--8<-- "project-structure.md"
```

## Creating a New Action

1. Create a new class in the `redstoneactions` package that extends `RedstoneAction`:
    ```java linenums="1"
    public class ExampleAction implements RedstoneAction {
        private static final Material material = Material.DIAMOND;
        private final String id;
        private final String label;
        private String[] description;

        public ExampleAction(String id, String label, String[] description) {
            this.id = id;
            this.label = label;
            this.description = description;
        }

        @Override
        public void execute(Player trigger) {
            Bukkit.broadcastMessage("Example action executed!");
        }

        @Override
        public ItemStack getIcon() {
            String title = ColorMapper.map(label);
            String[] lore = Arrays.stream(description).map(ColorMapper::map).toArray(String[]::new);

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(title);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);

            return item;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            
            map.put("id", id);
            map.put("type", ActionRegistry.getTypeName(this));
            map.put("label", label);
            map.put("description", description);

            return map;
        }

        public static ExampleAction deserialize(Map<?, ?> map) {
            String id = (String) map.get("id");
            String label = (String) map.get("label");
            ArrayList<String> description = (ArrayList<String>) map.get("description");
            String[] descriptionArray = description == null ? new String[0] : description.toArray(new String[0]);

            return new ExampleAction(id, label, descriptionArray);
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public String[] getDescription() {
            return description;
        }
    }   
    ```

2. Register your action in `RedhookPlugin.java`:
    ```java
    private void registerActionTypes() {
        ActionFactory.register(MyCustomAction.class, MyCustomAction::deserialize);
    }
    ```

3. Add documentation for your action in `docs/references/actions/`

## Development Workflow

1. Create a new branch for your feature:
    ```bash
    git checkout -b feature/my-new-feature
    ```

2. Make your changes and test them:
    - Use the provided test server setup
    - Test your changes thoroughly
    - Follow the code style guidelines

3. Build and test:
    ```bash
    ./gradlew build
    ```

4. Create a pull request:
    - Push your changes to GitHub
    - Create a pull request with a clear description
    - Wait for review and feedback

## Testing

1. Setup a local minecraft paper server

2. Copy the built .jar in your plugins directory

3. The test server will be available at `localhost:25565`

4. Use the following commands for testing:
    - `/redhook reload` - Reload the plugin
    - `/redhook wand` - Get the binding tool
    - `/redhook debug` - Gets you a debug tool for debugging actions

## Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods focused and small
- Document public APIs
- Write unit tests for new features (optional)

## Building for Production

1. Create a release build:
    ```bash
    ./gradlew build
    ```

2. Find the JAR file in `build/libs/`

3. Test the production build in a clean environment

## Building Documentation

The project uses MkDocs with the Material theme for documentation. Here's how to set up and build the documentation:

### Prerequisites

- Python 3.8 or later
- pip (Python package manager)

### Setup Documentation Environment

1. Create and activate a virtual environment:
    ```bash
    # Windows
    python -m venv .venv
    .venv\Scripts\activate

    # Linux/macOS
    python3 -m venv .venv
    source .venv/bin/activate
    ```

2. Install required packages:
    ```bash
    pip install -r requirements.txt
    ```

### Running Documentation

1. Start the development server:
    ```bash
    mkdocs serve
    ```
   This will start a local server at `http://127.0.0.1:8000`

2. Build the documentation:
    ```bash
    mkdocs build
    ```
   This creates a `site/` directory with the built documentation

### Automatic Documentation Builds

The documentation is automatically built and deployed through GitHub Actions:

1. Every push to the `main` branch triggers a documentation build
2. The built documentation is automatically deployed to GitHub Pages
3. The latest documentation is always available at `https://n1ck145.github.io/minecraft-redhook/`

You don't need to manually build and deploy the documentation. Just push your changes to the repository, and GitHub Actions will handle the rest.

### Troubleshooting

1. If the development server fails to start:
    - Check if the virtual environment is activated
    - Verify all dependencies are installed
    - Check for syntax errors in markdown files

2. If the build fails:
    - Check for invalid YAML syntax in mkdocs.yml
    - Verify all referenced files exist
    - Check for invalid markdown syntax

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a pull request

## Need Help?

- Check the [GitHub Issues](https://github.com/N1CK145/minecraft-redhook/issues)
- Review the [documentation](../index.md)