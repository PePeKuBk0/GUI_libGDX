package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.plaf.multi.MultiScrollBarUI;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen {

    public final Application app;

    private Stage stage;
    private Skin skin;
    private TextButton buttonPlay, buttonPlay2, buttonExit;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Texture background;
    private Music music;
    private Viewport viewport;
    private int backgroundOffset;


    public MainMenuScreen(final Application app) {
        this.app = app;
        this.stage = new Stage(new StretchViewport(Application.V_WIDHT,Application.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("imkv74m4q5g41.png"));
        viewport = new StretchViewport(Application.V_WIDHT,Application.V_HEIGHT, app.camera);
        backgroundOffset = 0;

        Music music = Gdx.audio.newMusic(Gdx.files.internal("MainMusic.mp3"));
        music.setVolume(0.2f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font",app.font);
        this.skin.load((Gdx.files.internal("uiskin.json")));

        initButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.0f, .0f, .0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        app.batch.begin();
        backgroundOffset++;
        if(backgroundOffset % 800 == 0){
            backgroundOffset = 0;
        }
        app.batch.draw(background, -backgroundOffset, 0, 800,500);
        app.batch.draw(background, -backgroundOffset + 800, 0, 800,500);
        app.batch.end();

        stage.draw();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);
        batch.setProjectionMatrix(app.camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void initButtons () {
        buttonPlay = new TextButton("DINO", skin,"default");
        buttonPlay.setPosition(35, 275);
        buttonPlay.setSize(200,60);
        buttonPlay.addAction(sequence(alpha(0),parallel(fadeIn(0.5f), moveBy(100, 0, .75f, Interpolation.pow5))));

        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.playScreen);
            }
        });

        buttonPlay2 = new TextButton("FLOPPY", skin,"default");
        buttonPlay2.setPosition(35, 175);
        buttonPlay2.setSize(200,60);
        buttonPlay2.addAction(sequence(alpha(0), delay(.5f),parallel(fadeIn(0.5f), moveBy(100, 0, .75f, Interpolation.pow5))));
        buttonPlay2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.playScreen2);
            }
        });

        buttonExit = new TextButton("EXIT", skin, "default");
        buttonExit.setPosition(35,75);
        buttonExit.setSize(200,60);
        buttonExit.addAction(sequence(alpha(0),delay(1f),parallel(fadeIn(0.5f), moveBy(100, 0, .75f, Interpolation.pow5))));

        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(buttonPlay);
        stage.addActor(buttonExit);
        stage.addActor(buttonPlay2);
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
