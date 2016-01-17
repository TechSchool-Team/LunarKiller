package br.com.techschool.lunarkiller.simulation;

import br.com.techschool.lunarkiller.model.Scenario;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import br.com.techschool.lunarkiller.model.Boss;
import br.com.techschool.lunarkiller.model.Bullet;
import br.com.techschool.lunarkiller.model.Character;

/*
 * Responsible for updating objects that occur during the game loop
 */
public class GameAction {

    // Contains scenario objects
    public Scenario scenario;
    public Boss boss;
    public Character character;
    public ArrayList<Bullet> bullets;

    // Points gained by player during game
    public int score;

    /*
     * Creates a GameAction object, initializing everything
     * that will be later updated.
     */
    public GameAction() {
        scenario = new Scenario();
        boss = new Boss();
        character = new Character(this);
        bullets = new ArrayList<Bullet>();
        score = 0;
    }

    /*
     * Updates all game loop objects.
     */
    public void update(float delta) {
        character.update(delta);
    	for(Bullet shot:bullets){
			shot.update(delta);
			if(shot.destroy){
				bullets.remove(shot);
			}
		}
    }
    
    public void shot(boolean strong){
		Vector3 dest = character.origin;
		Vector3 origin   = character.getGunPosition();
		Vector3 dir = new Vector3(0,0,0);
		dir.x = dest.x - origin.x;
		dir.y = dest.y - origin.y;
		dir.z = dest.z - origin.z;
		bullets.add(new Bullet(origin, dir.nor(), strong));
    }
}
