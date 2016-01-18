package br.com.techschool.lunarkiller.simulation;

import br.com.techschool.lunarkiller.model.Scenario;
import br.com.techschool.lunarkiller.screen.GameLoopScreen;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import br.com.techschool.lunarkiller.model.Boss;
import br.com.techschool.lunarkiller.model.Bullet;
import br.com.techschool.lunarkiller.model.Character;
import br.com.techschool.lunarkiller.model.Meteor;

/*
 * Responsible for updating objects that occur during the game loop
 */
public class GameAction {

    // Contains scenario objects
    public Scenario scenario;
    public Boss boss;
    public Character character;
    public ArrayList<Bullet> bullets;
    private ArrayList<Bullet> bulletsToDestroy;
    public ArrayList<Meteor> meteors;
    private ArrayList<Meteor> meteorsToDestroy;
    public GameLoopScreen gameLoop;
    
    // Points gained by player during game
    public int score;

    /*
     * Creates a GameAction object, initializing everything
     * that will be later updated.
     */
    public GameAction(GameLoopScreen gls) {
        scenario = new Scenario();
        boss = new Boss(this);
        character = new Character(this);
        bullets = new ArrayList<Bullet>();
        meteors = new ArrayList<Meteor>();
        score = 0;
        gameLoop = gls;
    }

    /*
     * Updates all game loop objects.
     */
    public void update(float delta) {
    	
        character.update(delta);
        boss.update(delta, character);
        
        bulletsToDestroy = new ArrayList<Bullet>();
    	for(Bullet shot:bullets){
			shot.update(delta, boss);
			if(shot.destroy){
				score += shot.addScore;
				bulletsToDestroy.add(shot);
			}
		}
    	
    	meteorsToDestroy = new ArrayList<Meteor>();
    	
    	for(Meteor rock:meteors){
    		rock.update(delta, character);
    		if(rock.destroy){
    			meteorsToDestroy.add(rock);
			}
    	}
    	
    	for(Meteor rock: meteorsToDestroy){
    		meteors.remove(rock);
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
    
    public void callMeteor(){
    	Vector3 dest = character.getPosition();
		Vector3 origin   = boss.handPosition;
		Vector3 dir = new Vector3(0,0,0);
		dir.x = dest.x - origin.x;
		dir.y = dest.y - origin.y - 12f;
		dir.z = dest.z - origin.z;
		meteors.add(new Meteor(origin, dir.nor()));
    }
}
