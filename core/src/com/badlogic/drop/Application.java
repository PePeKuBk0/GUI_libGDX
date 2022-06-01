package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;



public class Application extends Game {

    public static final int V_WIDHT = 480;
    public static final int V_HEIGHT = 420;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public Stage stage;
    public BitmapFont font;
    public AssetManager assets;
    public LoadingScreen loadingScreen;
    public SplashScreen splashScreen;
    public MainMenuScreen mainMenuScreen;
    public PlayScreen playScreen;
    public PlayScreen2 playScreen2;

    @Override
    public void create () {
        assets = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 720);

        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(V_WIDHT, V_HEIGHT, camera));

        font = new BitmapFont();

        loadingScreen = new LoadingScreen(this);
        splashScreen = new SplashScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        playScreen = new PlayScreen(this);
        playScreen2 = new PlayScreen2(this);

        this.setScreen(loadingScreen);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public  void resize (int wight, int height) {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        assets.dispose();
        loadingScreen.dispose();
        splashScreen.dispose();
        mainMenuScreen.dispose();
        playScreen.dispose();
        this.getScreen().dispose();
    }

}
