/**
 *  This class is the main class of "Kingdom of Guardia"
 *  You play as Sir Rumplebottom. The Kingdom of Guardia has been cursed by the evil Lord Vorkalth.
 *  The king has ordered you to go on a dangerous jounrey and defeat Lord Vorkalth.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 * 
 * @author  Andrew Helgeson
 * @version 2018.11.04
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Item helditem;
    public boolean hasLamp = false;
    public boolean hasSword = false;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        createItems();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room bedroom, castle_hallway, armory, throne_room, outside, pond, forest, cave, mysterious_room, cave_opening, cave_hole, 
        cave_east, vorkalth;
      
        // create the rooms
        bedroom = new Room("inside Sir Rumplebottom's castle bedroom.");
        castle_hallway = new Room("inside the main castle hallway.");
        armory = new Room("inside the castle's room full of weaponry and armor. You see a sword reflecting the sun's light, and you pick it up. You now have the Sword of Truth");
        throne_room = new Room("inside the king's throne room. You see the king sitting on his throne.");
        outside = new Room("outside the entrance of the castle.");
        pond = new Room("outside overlooking a large pond.");
        forest = new Room("inside a dark forest. You see a large opening to a cave.");
        cave = new Room("inside a pitch black cave. The only light you see is your lamp.");
        mysterious_room = new Room("inside a mysterious glowing part of the cave.");
        cave_opening = new Room("outside the cave. You are blocked by a large boulder.");
        cave_hole = new Room("inside a dark hole with no way out.");
        cave_east = new Room("inside the eastern part of the cave. There is a bright blue portal south of you. ");
        vorkalth = new Room("You go through the portal. ");
        
        
        
        
        // initialise room exits
        bedroom.setExit("west", castle_hallway);
        castle_hallway.setExit("north", throne_room);
        castle_hallway.setExit("east", bedroom);
        castle_hallway.setExit("west", armory);
        armory.setExit("east", castle_hallway);
        throne_room.setExit("south", castle_hallway);
        castle_hallway.setExit("south", outside);
        outside.setExit("north", castle_hallway);
        outside.setExit("east", pond);
        outside.setExit("south", forest);
        pond.setExit("west", outside);
        forest.setExit("south", cave);
        forest.setExit("north", outside);
        cave.setExit("west", mysterious_room);
        cave.setExit("south", cave_hole);
        cave.setExit("north", forest);
        cave.setExit("east", cave_east);
        mysterious_room.setExit("north", cave_opening);
        cave_opening.setExit("south", mysterious_room);
        cave_east.setExit("south", vorkalth);
        
        

        currentRoom = bedroom;
        
        
        if (currentRoom == armory)
        {
            hasSword = true;
        }
        
        if (currentRoom == throne_room)
        {
            hasLamp = true;
        }
        
    }
    
    private void createItems()
    {
        // create items
        Item sword, lamp;
        
        sword = new Item("Sword of Truth", 5);
        lamp = new Item("Lamp", 1);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Kingdom of Guardia!");
        System.out.println("You are valiant knight named Sir Rumplebottom.");
        System.out.println("The king has ordered you to put a stop to the evil Lord Vorkalth, who has cursed your land");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case LOOK:
                look();
                break;
                

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are valiant knight named Sir Rumplebottom.");
        System.out.println("Your kingdom has been cursed by the Evil Lord Vorkalth.");
        System.out.println("The kingdom has fallen into chaos.");
        System.out.println("No longer do truth and justice reign supreme;");
        System.out.println("instead, your home has fallen into chaos.");
        System.out.println("It is up to you to thwart Lord Vorkalth's evil scheme.");
        System.out.println("You decide to embark on this dangerous quest");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
        
    }
    
    private void look()
    {
        //look method prints out the long description of current room.
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
