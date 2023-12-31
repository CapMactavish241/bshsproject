import java.io.*;
import java.util.*;

class Player {
    private String name;
    private int roundsPlayed;
    private int score;

    public Player(String name) {
        this.name = name;
        roundsPlayed = 0;
        score = 0;
    }

    public String getName() {
        return name;
    }

    public int getRoundCount() {
        return roundsPlayed;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoundCount(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void addRound() {
        roundsPlayed++;
    }
}

class PlayerManager {
    private Player[] players;

    public PlayerManager() {
        players = new Player[0];
    }

    public void addPlayer(String name) {
        Player player = new Player(name);
        add(player);
    }

    public void deletePlayer(String name) {
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.getName().equalsIgnoreCase(name)) {
                remove(i);
                break;
            }
        }
    }

    public void add(Player player) {
        Player[] tempList = players;
        players = new Player[tempList.length + 1];
        System.arraycopy(tempList, 0, players, 0, tempList.length);
        players[players.length - 1] = player;
    }

    public void remove(int index) {
        Player[] bufferArray = new Player[players.length - 1];
        int bufferIndex = 0;
        for (int i = 0; i < players.length; i++) {
            if (i != index) {
                bufferArray[bufferIndex] = players[i];
                bufferIndex++;
            }
        }
        players = bufferArray;
    }

    public void write(File fl) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        for (Player player : players) {
            dos.writeUTF(player.getName());
            dos.writeInt(player.getRoundCount());
            dos.writeInt(player.getScore());
        }
        dos.close();
    }

    public void read(File fl) throws IOException {
        fl.createNewFile();
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        players = new Player[0];
        while (true) {
            try {
                String name = dis.readUTF();
                int roundCount = dis.readInt();
                int score = dis.readInt();
                Player player = new Player(name);
                player.setRoundCount(roundCount);
                player.setScore(score);
                add(player);
            } catch (EOFException e) {
                break;
            }
        }
        dis.close();
    }

    public boolean isPlayer(String name) {
        for (Player player : players)
            if (player.getName().equalsIgnoreCase(name)) return true;
        return false;
    }

    public int getPlayerIndex(String name) {
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.getName().equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void displayAllPlayers() {
        for (int i = 0; i < players.length; i++)
            System.out.println("<" + i + "> " + players[i].getName());
        if (players.length == 0)
            System.out.println("No players found...");
    }

    public void displayPlayerCard(String name) {
        for (Player player : players)
            if (player.getName().equalsIgnoreCase(name)) {
                System.out.println("Name: " + player.getName());
                System.out.println("Score: " + player.getScore());
                System.out.println("Rounds Played: " + player.getRoundCount());
                System.out.println("Rounds Won: " + (player.getScore() / 100));
                System.out.println();
            }
    }
}

class Game {
    private final String[] wordList;
    private final PlayerManager manager;
    private final Scanner sc;

    public Game(String[] wordList, PlayerManager manager, Scanner sc) {
        this.sc = sc;
        this.wordList = wordList;
        this.manager = manager;
    }

    public void play() {
        int player = -1;
        int c = 0;
        do {
            if (c > 0) System.out.println("No such player found, try again...");
            System.out.println("Who's playing?");
            String name = sc.nextLine();
            for (int i = 0; i < manager.getPlayers().length; i++)
                if (manager.getPlayers()[i].getName().equalsIgnoreCase(name)) player = i;
            c++;
        } while (player == -1);
        manager.getPlayers()[player].addRound();
        char[] word = wordList[(int) (Math.random() * wordList.length)].toCharArray();
        char[] guessedWord = new char[word.length];
        for (int i = 0; i < guessedWord.length; i++) guessedWord[i] = '_';
        int lives = 0;
        do {
            if (arrayEqual(word, guessedWord)) break;
            System.out.println("Guesses left: " + (5-lives) + "/5");
            System.out.print("Guessed word:");
            for (char ch : guessedWord) System.out.print(ch + " ");
            System.out.println("\nGuess a letter: ");
            char guess = sc.next().charAt(0);
            if (arrayContains(guessedWord, guess)) {
                System.out.println("You've already guessed this letter");
                continue;
            }
            boolean correct = false;
            for (int i = 0; i < word.length; i++) {
                if ((word[i] + "").equalsIgnoreCase(guess + "")) {
                    guessedWord[i] = word[i];
                    correct = true;
                }
            }
            if(!correct) lives++;

        } while (lives <= 5);
        if (arrayEqual(word, guessedWord)) {
            manager.getPlayers()[player].addScore(100);
            System.out.println("Congrats! You've been awards 100 score points");
        } else System.out.println("Oops! You ran out of lives.");
    }

    boolean arrayEqual(char[] array1, char[] array2) {
        if (array1.length == array2.length) {
            boolean equal = true;
            for (int i = 0; i < array1.length; i++) {
                if (!(array1[i] + "").equalsIgnoreCase(array2[i] + "")) {
                    equal = false;
                    break;
                }
            }
            return equal;
        } else return false;
    }

    boolean arrayContains(char[] array, char letter) {
        boolean contains = false;
        for (char letters : array) {
            if (letters == letter) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}

class WordList {
    private String[] wordlist;
    private final File wordListFile;

    WordList(File wordListFile) {
        wordlist = new String[0];
        this.wordListFile = wordListFile;
    }

    public void write() throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(wordListFile));
        for (String word : wordlist)
            dos.writeUTF(word);
        dos.close();
    }

    public void read() throws IOException {
        wordListFile.createNewFile();
        DataInputStream dis = new DataInputStream(new FileInputStream(wordListFile));
        wordlist = new String[0];
        while (true) {
            try {
                String word = dis.readUTF();
                add(word);
            } catch (EOFException e) {
                break;
            }
        }
        dis.close();
    }

    public void add(String word) {
        String[] tempList = wordlist;
        wordlist = new String[tempList.length + 1];
        System.arraycopy(tempList, 0, wordlist, 0, tempList.length);
        wordlist[wordlist.length - 1] = word;
    }

    public void delete() {
        wordListFile.deleteOnExit();
        System.out.println("Word List deleted, exiting program...");
        System.exit(0);
    }

    public String[] getWords() {
        if (wordlist.length == 0) return new String[]{"hangman", "game", "rose", "aditya", "sahoo"};
        return wordlist;
    }
}

public class test2 {
    static File fl;
    static Scanner sc;
    static PlayerManager playerManager;
    static Game game;
    static WordList wordList;

    public static void main(String[] args) throws IOException {
        fl = new File("hangman.dat");
        fl.createNewFile();
        sc = new Scanner(System.in);
        playerManager = new PlayerManager();
        wordList = new WordList(new File("wordlist.dat"));
        mainMenu();
    }

    static void mainMenu() {
        while (true) {
            try {
                playerManager.read(fl);
                wordList.read();
            } catch (IOException ignored) {
            }
            game = new Game(wordList.getWords(), playerManager, sc);
            int ch = menu("Hangman", getMainMenuOptions());
            switch (ch) {
                case 1: {
                    System.out.print("Enter player name: ");
                    String name = sc.nextLine();
                    if (playerManager.isPlayer(name)) {
                        System.out.println("Player with name " + name + " already exists");
                        break;
                    }
                    playerManager.addPlayer(name);
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes");
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter player current name: ");
                    String oldName = sc.nextLine();
                    if (playerManager.isPlayer(oldName)) {
                        System.out.println("Enter new name for player");
                        String newName = sc.nextLine();
                        if (playerManager.isPlayer(newName)) {
                            System.out.println("Player with name " + newName + " already exists");
                        } else {
                            playerManager.getPlayers()[playerManager.getPlayerIndex(newName)].setName(newName);
                        }
                    } else {
                        System.out.println("No such player found");
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter player name: ");
                    String name = sc.nextLine();
                    playerManager.deletePlayer(name);
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes");
                    }
                    break;
                }
                case 4: {
                    game.play();
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save game data");
                    }
                    break;
                }
                case 5: {
                    System.out.print("Enter player name to reset: ");
                    String name = sc.nextLine();
                    playerManager.deletePlayer(name);
                    playerManager.addPlayer(name);
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes");
                    }
                    break;
                }
                case 6:
                    playerManager.displayAllPlayers();
                    break;
                case 7: {
                    System.out.print("Enter player name to view card: ");
                    String name = sc.nextLine();
                    playerManager.displayPlayerCard(name);
                    break;
                }
                case 8: {
                    System.out.println("Enter word to add");
                    String word = sc.nextLine();
                    wordList.add(word);
                    try {
                        wordList.write();
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes");
                    }
                    break;
                }
                case 9:
                    wordList.delete();
                    break;
                default: {
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                }
            }
        }
    }

    static int menu(String header, String[] options) {
        int choice;
        int c = 0;
        System.out.println("\n================= * " + header + " * =================");
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again");
            boolean hasExitOrGoBack = options[0].equalsIgnoreCase("Exit") || options[0].equalsIgnoreCase("Go Back");
            for (int i = 0; i < options.length; i++) {
                if (hasExitOrGoBack && i == 0) continue;
                printOptions(("================= * " + header + " * =================").length(), i, options[i]);
            }
            System.out.println();
            if (hasExitOrGoBack)
                printOptions(("================= * " + header + " * =================").length(), 0, options[0]);
            System.out.println();
            System.out.print("Select an option to proceed: ");
            choice = sc.nextInt();
            c++;
        } while (choice < 0 || choice >= options.length);
        for (int i = 0; i < ("================= * " + header + " * =================").length(); i++)
            System.out.print("=");
        System.out.println();
        sc.nextLine();
        return choice;
    }

    static void printOptions(int heaaderLength, int opNumber, String option) {
        String space = "  ";
        if (opNumber > 9) space = " ";
        for (int i = 1; i <= heaaderLength / 6; i++) System.out.print(" ");
        System.out.println(opNumber + "." + space + option);
    }

    static String[] getMainMenuOptions() {
        String[] op = new String[10];
        op[0] = "Exit";
        op[1] = "Add Player";
        op[2] = "Rename Player";
        op[3] = "Remove Player";
        op[4] = "Play";
        op[5] = "Reset Player Statistics";
        op[6] = "View All Players";
        op[7] = "View Player Card";
        op[8] = "Add Word to Wordlist";
        op[9] = "Delete Wordlist";
        return op;
    }
}
