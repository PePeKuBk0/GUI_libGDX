package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {

    public final Application app;
    private Stage stage;
    private Skin skin;
    private TextButton button;

    private Image splashImg;

    public SplashScreen(final Application app) {
        this.app = app;
        this.stage = new Stage();
        stage = new Stage(new StretchViewport(Application.V_WIDHT, Application.V_HEIGHT, app.camera));
    }

    @Override
    public void show() {
        System.out.println("Show");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font",app.font);
        this.skin.load((Gdx.files.internal("uiskin.json")));

        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {
                app.setScreen(app.mainMenuScreen);
            }
        };

        Texture spashTex = app.assets.get("retroland_300x300.jpg", Texture.class);
        splashImg = new Image(spashTex);

        splashImg.setPosition(100,150);

        splashImg.addAction(sequence(alpha(0f), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1.25f, 1.25f, 2.5f, Interpolation.pow5 ),
                        moveTo(50,0, 2f, Interpolation.swing)),
                delay(1f),fadeOut(1f), run(transitionRunnable)));

        stage.addActor(splashImg);
        initButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.0f, .0f, .0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,false);
    }

    @Override
    public void pause() {
        System.out.println("Pause");
    }

    @Override
    public void resume() {
        System.out.println("Resume");
    }

    @Override
    public void hide() {
        System.out.println("Hide");
    }

    @Override
    public void dispose() {
        System.out.println("Dispose");
        stage.dispose();
    }
    public void initButtons () {
        button = new TextButton("DNGAMES PRESENTS", skin, "default");
        button.setPosition(45, 300);
        button.addAction(sequence(sequence(alpha(0f), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                    scaleTo(1.25f, 1.25f, 2.5f, Interpolation.pow5)),
                    delay(1f),fadeOut(1f))));
        button.setTouchable(Touchable.disabled);
        stage.addActor(button);
    }
}
