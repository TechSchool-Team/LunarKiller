package br.com.techschool.lunarkiller.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class StartMenu {

    // Handles button processing
    private Stage stage;

    // Font used on button text
    private BitmapFont font;

    // Appearance of each button
    private Skin skin;

    // Appearance of the button
    private TextButtonStyle buttonStyle;

    // The buttons themselves
    private TextButton play, quit, credits;

    // Width and height for each button
    private final float buttonWidth  = Constant.GAME_WIDTH/4;
    private final float buttonHeight = Constant.GAME_HEIGHT/10;

    // Initial height that button sequence starts
    private final float sequenceHeight = Constant.GAME_HEIGHT/2;

    // Difference in height between each button
    private final float dy = buttonHeight/3;

    public StartMenu() {
        // Set stage, with viewport equal to screen default size,
        // to take care of input actions
        stage = new Stage(new StretchViewport(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        // Configure font and skin
        font = new BitmapFont(Gdx.files.internal("fonts/debug.fnt"));
        skin = new Skin();
        skin.add("default", font);

        // Create texture for skin
        Pixmap pixmap = new Pixmap((int) buttonWidth,
                                   (int) buttonHeight,
                                   Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        // Configure text button style
        buttonStyle = new TextButtonStyle();
        buttonStyle.up = skin.newDrawable("background", Color.GRAY);
        buttonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.checked = skin.newDrawable("background", Color.GRAY);
        buttonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = skin.getFont("default");
        skin.add("default", buttonStyle);

        // Create buttons
        play    = new TextButton("PLAY", buttonStyle);
        quit    = new TextButton("QUIT", buttonStyle);
        credits = new TextButton("CREDITS", buttonStyle);

        // Center each button on x axis, and calculate y coordinate
        // for the first button
        float x = Constant.GAME_WIDTH/2 - buttonWidth/2;
        float y = sequenceHeight - buttonHeight/2;

        // Set position of each button
        play.setPosition(x, y);
        quit.setPosition(x, y - buttonHeight - dy);
        credits.setPosition(x, y - 2*buttonHeight -2*dy);

        // Add buttons to the stage
        stage.addActor(play);
        stage.addActor(quit);
        stage.addActor(credits);
    }

    public void update(float delta) {
        // Update viewport size before updating each actor
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.act(delta);
    }

    public void draw(float delta) {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
