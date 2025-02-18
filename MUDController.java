import java.util.*;

class Item {
    private final String name;
    private final String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

class Room {
    private final String name;
    private final String description;
    private final Map<String, Room> directions;
    private final List<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.directions = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public String describe() {
        StringBuilder roomDescription = new StringBuilder(name + "\n" + description + "\nItems here: ");
        if (items.isEmpty()) {
            roomDescription.append("None");
        } else {
            for (Item item : items) {
                roomDescription.append(item.getName()).append(" ");
            }
        }
        return roomDescription.toString();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item getItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public void connectRoom(String direction, Room room) {
        directions.put(direction, room);
    }

    public Room getRoomInDirection(String direction) {
        return directions.get(direction);
    }
}

class Player {
    private final String name;
    private Room currentRoom;
    private final List<Item> inventory;

    public Player(String name) {
        this.name = name;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void addToInventory(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }
}


public class main {

    private final Player player;
    private boolean running;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
    }

    public void runGameLoop() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine();
            handleInput(input);
        }
        scanner.close();
    }

    public void handleInput(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                pickUp(argument);
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                System.out.println("Exiting the game...");
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.describe());
    }

    private void move(String direction) {
        Room currentRoom = player.getCurrentRoom();
        Room nextRoom = currentRoom.getRoomInDirection(direction);

        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("You moved " + direction);
            System.out.println(nextRoom.describe());
        } else {
            System.out.println("You can't go that way!");
        }
    }

    private void pickUp(String arg) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(arg);

        if (item != null) {
            player.addToInventory(item);
            currentRoom.removeItem(item);
            System.out.println("You pick up the " + arg);
        } else {
            System.out.println("No item named " + arg + " here!");
        }
    }

    private void checkInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for (Item item : player.getInventory()) {
                System.out.println(item.getName());
            }
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Look around the room");
        System.out.println("move <forward|back|left|right> - Move in a direction");
        System.out.println("pick up <itemName> - Pick up an item from the room");
        System.out.println("inventory - List your inventory");
        System.out.println("help - Show this help message");
        System.out.println("quit/exit - Exit the game");
    }

    public static void main(String[] args) {
        Player player = new Player("Hero");
        Room room1 = new Room("Small Stone Chamber", "A small, beautiful.");
        Room room2 = new Room("Grand Hallway", "A long,beautiful.");
        Item sword = new Item("sword", "A sharp, beautiful.");
        room1.addItem(sword);

        room1.connectRoom("forward", room2);

        player.setCurrentRoom(room1);

        MUDController controller = new MUDController(player);
        controller.runGameLoop();
    }
}
