package br.com.techschool.lunarkiller.simulation;

import br.com.techschool.lunarkiller.model.Scenario;

/*
 * Responsible for updating objects that occur during the game loop
 */
public class GameAction {

    // Contains scenario objects
    public Scenario scenario;

    /*
     * Creates a GameAction object, initializing everything
     * that will be later updated.
     */
    public GameAction() {
        scenario = new Scenario();
    }

    /*
     * Updates all game loop objects.
     */
    public void update(float delta) {
        // TODO: Check what to do here
    }
}
