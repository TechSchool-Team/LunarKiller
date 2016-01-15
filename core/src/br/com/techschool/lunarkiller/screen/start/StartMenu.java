package br.com.techschool.lunarkiller.screen.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import br.com.techschool.lunarkiller.util.Constant;

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

    // Centers menu button for the stage
    private Table table;

    // Font used on button text
    private BitmapFont font;

    // Image of the buttons
    private TextureAtlas buttonAtlas;

    // Configurable skin for all buttons
    private Skin skin;

    // Configuration used for all buttons
    private TextButtonStyle buttonStyle;

    // Button array, one for each Command
    private TextButton[] textButtons;

    // Width and height for each button
    private final float buttonWidth  = Constant.GAME_WIDTH/4;
    private final float buttonHeight = Constant.GAME_HEIGHT/10;

    // Difference in height between each button
    private final float buttonDelta = buttonHeight/3;

    public StartMenu() {
        // Set stage, with viewport equal to screen default size,
        // to take care of input actions
        stage = new Stage(new StretchViewport(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        table = new Table();

        // Load atlas and font
        font = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
        buttonAtlas = new TextureAtlas(Gdx.files.internal("textures/ui-gray.atlas"));

        skin = new Skin();
        skin.addRegions(buttonAtlas);  // Add on/off skins
        skin.add("default", font);

        // Configure text button style
        buttonStyle = new TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button_03");
        buttonStyle.down = skin.getDrawable("button_02");
        buttonStyle.over = skin.getDrawable("button_01");
        buttonStyle.font = skin.getFont("default");
        skin.add("default", buttonStyle);

        textButtons = new TextButton[Command.size];

        // Button creation
        for (Command command : Command.values()) {
            // Create text button
            int i = command.ordinal();
            textButtons[i] = new TextButton(command.toString(), buttonStyle);
            // Configure button position
            table.add(textButtons[i])
                 .width(buttonWidth)
                 .height(buttonHeight)
                 .padTop(buttonDelta);
            // Go to next line
            table.row();
        }

        table.setFillParent(true);
        stage.addActor(table);
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
