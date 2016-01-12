package br.com.techschool.lunarkiller.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

/*
 * Stores game ranks for future searches.
 */
public class Rank {

    // Root node for the Binary Search Tree.
    private Node root;

    // Name of text file containing ranks
    private static final String RANK_FILE = "/br/com/techschool/lunarkiller/util/Ranks.txt";

    /*
     * Ranks are stored as a Binary Search Tree (BST)
     * containing several Nodes.
     */
    private class Node {
        private int points;
        private String name;
        private Node left, right;

        /*
         * Creates a Node with the given parameters.
         */
        public Node(int points, String name) {
            this.points = points;
            this.name = name;
        }
    }

    /*
     * Reads and stores all available ranks from a file.
     */
    public Rank() {
        // Get file in same directory as this class
        URL url = getClass().getResource(RANK_FILE);
        File file = new File(url.getPath());
        Scanner scanner;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Rank file not found!");
            return;
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Split line into points and rank name
            String[] parts = line.split("\\s+", 2);
            put(Integer.parseInt(parts[0]), parts[1]);
        }

        scanner.close();
    }

    /*
     * Inserts a new rank with the given points and name.
     */
    public void put(int points, String name) {
        root = put(root, points, name);
    }

    /*
     * Inserts a new node with the given points and rank name into the
     * subtree with Node x as root. Returns the root of the BST.
     * If a node with the given points exists, its name is overwritten with
     * the new name value.
     */
    private Node put(Node x, int points, String name) {
        if (x == null) {
         // Return the root of the new tree
            return new Node(points, name);
        }

        if (points < x.points)
            x.left  = put(x.left, points, name);
        else if (points > x.points)
            x.right = put(x.right, points, name);
        else {
            // Update rank name of same point value
            x.name = name;
        }

        return x;
    }

    /*
     * Returns the rank attained for the given score.
     * Rank attained is always the closest for the given score's floor.
     */
    public String getRank(int score) {
        Node x = floor(root, score);
        if (x == null)
            return "";  // Empty String to avoid problems :P
        return x.name;
    }

    /*
     * Returns the rank node whose points are the floor of the given score.
     * Node x represents the root of a subtree.
     */
    private Node floor(Node x, int score) {
        if (x == null) {
            // Empty BST
            return null;
        }

        if (score == x.points)
            return x;
        if (score < x.points)
            return floor(x.left, score);

        // Case score > x.points; check if higher rank exists first
        Node higherRank = floor(x.right, score);
        return (higherRank != null ? higherRank : x);
    }
}
