package br.com.techschool.lunarkiller.screen.end;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

/*
 * Reads, stores and updates players with highest achieved scores.
 */
public class HallFame {

    // Stores top players
    private LinkedList<Player> topPlayers;

    // Number of players in ranking
    private final int SIZE = 10;

    // File containing top players and their scores
    private final String HALLFAME_FILE = "HallFame.txt";

    private class Player {
        private String name;
        private int points;

        /*
         * Creates a Node with the given parameters.
         */
        public Player(String name, int points) {
            this.name = name;
            this.points = points;
        }
    }

    public HallFame() {
        topPlayers = new LinkedList<Player>();

        // Get file in same directory as this class
        URL url = getClass().getResource(HALLFAME_FILE);
        File file = new File(url.getFile());
        Scanner scanner;

        // Create empty file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error when creating empty hall of fame file!");
                return;
            }
        }

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error when reading hall of fame file!");
            return;
        }

        for (int i = 0; scanner.hasNextLine() && i < SIZE; i++) {
            String line = scanner.nextLine();
            // Separate name from points according to last space character
            int spacePos = line.lastIndexOf(' ');
            String name = line.substring(0, spacePos - 1);
            int points = Integer.parseInt(line.substring(spacePos + 1));
            // Update top players list
            Player player = new Player(name, points);
            topPlayers.add(player);
        }

        scanner.close();
    }

    /*
     * Returns a big String containing a player name and their
     * respective score per line.
     */
    public String getTopPlayers() {
        String str = "";

        for (int i = 0; i < topPlayers.size(); i++) {
            Player player = topPlayers.get(i);
            str += player.name + " " + player.points;
        }

        return str;
    }

    /*
     * Checks if the player with the specified name and score has
     * entered the hall of fame. If true, updates and returns true;
     * otherwise, returns false.
     */
    public boolean update(String name, int score) {
        for (int i = 0; i < topPlayers.size(); i++) {
            if (topPlayers.get(i).points < score) {
                // Add new player to position 'i'
                topPlayers.add(i, new Player(name, score));
                if (topPlayers.size() > SIZE) {
                    // Control list size by removing final player
                    topPlayers.removeLast();
                }
                return true;
            }
        }

        return false;
    }

    /*
     * Writes the current hall of fame in a text file.
     */
    public void writeFile() {
        // Get file in same directory as this class
        URL url = getClass().getResource(HALLFAME_FILE);
        File file = new File(url.getPath());

        try {
            // Overwrite existing file
            FileWriter writer = new FileWriter(file, false);
            for (Player player : topPlayers) {
                writer.write(player.name + " " + player.points);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error when updating hall of fame file!");
            return;
        }
    }
}
