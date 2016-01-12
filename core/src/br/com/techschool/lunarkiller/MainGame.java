package br.com.techschool.lunarkiller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import br.com.techschool.lunarkiller.screen.GameLoopScreen;
import br.com.techschool.lunarkiller.screen.GenericScreen;
import br.com.techschool.lunarkiller.screen.end.EndScreen;
import br.com.techschool.lunarkiller.screen.start.StartScreen;

/*
 * Related to the creation of a Lunar Killer game.
 * Manages which screen is currently active.
 */
public class MainGame extends Game {

    // Currently active screen
    private GenericScreen currentScreen;

    @Override
    public void create() {

        // Creates and sets the start screen
        currentScreen = new StartScreen("START");
        setScreen(currentScreen);
    }

    @Override
    public void render() {
        // Render screen according to time between frames
        float delta = Gdx.graphics.getDeltaTime();
        currentScreen = (GenericScreen) getScreen();
        currentScreen.render(delta);

        // Switch screen if current one is done
        if (currentScreen.isDone()) {
            switch (currentScreen.getName()) {
                case "START":
                    setScreen(new GameLoopScreen("GAMELOOP"));
                    break;
                case "GAMELOOP":
                    int score = ((GameLoopScreen) getScreen()).getScore();
                    setScreen(new EndScreen("END", score));
                    break;
                case "END":
                    setScreen(new StartScreen("START"));
                    break;
            }
        }
    }
}
