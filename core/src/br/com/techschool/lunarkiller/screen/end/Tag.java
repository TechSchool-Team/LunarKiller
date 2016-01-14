package br.com.techschool.lunarkiller.screen.end;

/*
 * Simple class used for storing ranks and players.
 */
public class Tag {

    // The player's name
    public String name;

    // Score related to the name
    public int points;

    /*
     * Creates a Tag with the given parameters.
     */
    public Tag(String name, int points) {
        this.name = name;
        this.points = points;
    }
}
