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

/*
 * Manages the main menu used on the start screen.
 */
public class StartMenu {

    /*
     * Contains all possible commands to be used as buttons on this menu
     */
    public enum Command {
        START, CREDITS, QUIT;

        // Number of elements in this enum
        private static final int size = Command.values().length;
    };

    // Handles button processing
    private Stage stage;

    // Font used on button text
    private BitmapFont font;

    // Configurable skin for all buttons
    private Skin skin;

    // Configuration used for all buttons
    private TextButtonStyle buttonStyle;

    // Button array, one for each Command
    private TextButton[] textButtons;

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

        // Center each button on x axis;
        // calculate y coordinate for the first button
        float x = Constant.GAME_WIDTH/2 - buttonWidth/2;
        float y = sequenceHeight - buttonHeight/2;

        textButtons = new TextButton[Command.size];

        // Configure each button
        for (Command command : Command.values()) {
            int i = command.ordinal();
            // Create, position and add button to stage
            textButtons[i] = new TextButton(command.toString(), buttonStyle);
            textButtons[i].setPosition(x, y - i*(buttonHeight + dy));
            stage.addActor(textButtons[i]);
        }
    }

    /*
     * Updates location and state for each button on the stage.
     */
    public void update(float delta) {
        // Update viewport size before updating each actor
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.act(delta);
    }

    /*
     * Draws menu buttons on the screen.
     */
    public void draw(float delta) {
        stage.draw();
    }

    /*
     * Sets the checked state of the specified command's button equivalent.
     */
    public void setButtonChecked(Command command, boolean checked) {
        int i = command.ordinal();
        textButtons[i].setChecked(checked);
    }

    /*
     * Returns true if button of the specified command is checked,
     * or false otherwise.
     */
    public boolean isButtonChecked(Command command) {
        int i = command.ordinal();
        return textButtons[i].isChecked();
    }

    /*
     * Clears memory used by this menu.
     */
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
