package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class LoadingScreen implements Screen {

    private final Application app;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private float progress;

    public LoadingScreen(final Application app) {
        this.app = app;
        stage = new Stage(new StretchViewport(Application.V_WIDHT, Application.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        queueAssets();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.0f, .0f, .0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(170,160, app.camera.viewportWidth - 64, 16);
        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.rect(170,160, progress * (app.camera.viewportWidth - 64),  16);
        shapeRenderer.end();

        app.batch.begin();
        app.font.draw(app.batch,"LOADING", 350,120);
        app.batch.end();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, app.assets.getProgress(), .1f);
        if(app.assets.update() && progress >= app.assets.getProgress() - .001f) {
            app.setScreen(app.splashScreen);
        }
    }

    @Override
    public void resize(int width, int height) {

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


    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void queueAssets() {
        app.assets.load("retroland_300x300.jpg", Texture.class);
        app.assets.load("imkv74m4q5g41.png", Texture.class);
        app.assets.load("uiskin.atlas", TextureAtlas.class);
        app.assets.load("uiskin.json", Skin.class);
    }

}
