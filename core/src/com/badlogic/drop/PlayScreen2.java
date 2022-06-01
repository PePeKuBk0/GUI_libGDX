package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class PlayScreen2  implements Screen {

    private Texture coinsImage;
    private Texture flappyImage;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Array<Rectangle> toptube;
    private Array<Rectangle> bottube;
    private Array<Rectangle> rarrow;
    private boolean Arrow = false;
    private Rectangle flapy;
    private Array<Rectangle> coins;
    private long lastSpawnTime;
    private final float gravity = 0.2f;
    private Texture topImage;
    private Texture botImage;
    private int score;
    private String vypisScore;
    BitmapFont scoreFont;
    BitmapFont gOverFont;
    BitmapFont startFont;
    private Texture backImage;
    private int state = 4;
    private float velocity = 5f;
    private boolean Dead = true;
    private int highScore;
    private String vypisHighScore;
    private int speed = 200;
    private int dificulty;
    private Texture rarrowImage;
    private int kocka;
    Random rand = new Random();
    private int L = 2500;
    private Skin skin;
    private Sound flap;
    private Sound oof;
    private Sound coinPickUp;

    private final Application app;
    private Stage stage;

    public PlayScreen2(final Application app) {
        this.app = app;
        this.stage = new Stage(new StretchViewport(Application.V_WIDHT, Application.V_HEIGHT, app.camera));
    }

    @Override
    public void show() {
        coinsImage = new Texture(Gdx.files.internal("coin.png"));
        flappyImage = new Texture(Gdx.files.internal("Flappy-Bird-PNG-Photos_48x48.png"));
        topImage = new Texture(Gdx.files.internal("toptube.png"));
        botImage = new Texture(Gdx.files.internal("bottube.png"));
        backImage = new Texture(Gdx.files.internal("FPbackground.png"));
        rarrowImage = new Texture(Gdx.files.internal("rarrow.png"));
        flap = Gdx.audio.newSound(Gdx.files.internal("flap.mp3"));
        oof = Gdx.audio.newSound(Gdx.files.internal("roblox.mp3"));
        coinPickUp = Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));

        score = 0;
        vypisScore = "score: 0";
        vypisHighScore = "Highscore: 0";
        scoreFont = new BitmapFont();
        gOverFont = new BitmapFont();
        startFont = new BitmapFont();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        flapy = new Rectangle();
        flapy.x = 150 - 64;
        flapy.y = 200;
        flapy.width = 44;
        flapy.height = 44;

        coins = new Array<Rectangle>();
        bottube = new Array<Rectangle>();
        toptube = new Array<Rectangle>();
        rarrow = new Array<Rectangle>();
        kocka = rand.nextInt(150) + 180;

        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font);
        this.skin.load((Gdx.files.internal("uiskin.json")));
    }

    private void spawnArrow() {
        Rectangle rArrow = new Rectangle();
        rArrow.x = 800 - 64;
        rArrow.y = kocka - 35;
        rarrow.add(rArrow);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    private void spawnTube() {

        Rectangle ttube = new Rectangle();

        if (Arrow == true) {
            ttube.y = kocka;
            ttube.x = L;
        } else {

            ttube.y = rand.nextInt(160) + 160;
            ttube.x = 800 - 64;
        }
        ttube.height = ttube.y;
        ttube.width = 50;
        toptube.add(ttube);

        Rectangle btube = new Rectangle();

        btube.y = ttube.y - 440;
        if (Arrow == true) {

            btube.x = L;
        } else btube.x = 800 - 64;

        btube.height = 310;
        btube.width = 50;
        bottube.add(btube);

        Rectangle coin = new Rectangle();

        coin.y = ttube.y - 80;

        if (Arrow == true) {
            coin.x = L;
        } else coin.x = 800 - 64;
        coin.width = 48;
        coin.height = 48;
        coins.add(coin);

        lastSpawnTime = TimeUtils.nanoTime();
    }

    private void Death() {
        state = 4;
        score = 0;
        flapy.x = 150 - 64;
        flapy.y = 200;
        velocity = 0;
        Arrow = false;

        vypisScore = "score: " + score;
        batch.begin();

        scoreFont.draw(batch, vypisScore, 10, 470);
        batch.end();

        for (Iterator<Rectangle> birdIter = toptube.iterator(); birdIter.hasNext(); ) {
            Rectangle ttube = birdIter.next();
            if (ttube.x > 0) birdIter.remove();
        }

        for (Iterator<Rectangle> iter = bottube.iterator(); iter.hasNext(); ) {
            Rectangle btube = iter.next();
            if (btube.x > 0) iter.remove();
        }
        for (Iterator<Rectangle> iter = coins.iterator(); iter.hasNext(); ) {
            Rectangle coin = iter.next();
            if (coin.x > 0) iter.remove();
        }
        for (Iterator<Rectangle> iter = rarrow.iterator(); iter.hasNext(); ) {
            Rectangle coin = iter.next();
            if (coin.x > 0) iter.remove();
        }

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch

        batch.begin();
        batch.draw(backImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        scoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        scoreFont.draw(batch, vypisScore, 10, 470);

        scoreFont.draw(batch, vypisHighScore, 700, 470);

        batch.draw(flappyImage, flapy.x, flapy.y);
        for (Rectangle coinr : coins) {
            batch.draw(coinsImage, coinr.x, coinr.y);
        }
        for (Rectangle tube : toptube) {
            batch.draw(topImage, tube.x, tube.y);
        }
        for (Rectangle btube : bottube) {
            batch.draw(botImage, btube.x, btube.y);
        }
        for (Rectangle arow : rarrow) {
            batch.draw(rarrowImage, arow.x, arow.y);
        }

        batch.end();

        highScore = prefs.getInteger("highscore");
        if (highScore < score) {
            highScore = score;
            prefs.putInteger("highscore", highScore);
            prefs.flush();
        }
        vypisHighScore = "Highscore: " + highScore;
        if (state == 4) {
            if (TimeUtils.nanoTime() - lastSpawnTime > 250000000) ;
            batch.begin();
            startFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            startFont.draw(batch, "Chose dificulty!", 370, 240);
            startFont.draw(batch, "Normal: N", 350, 220);
            startFont.draw(batch, "Scale: S", 430, 220);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Keys.N)) {
                state = 0;
                dificulty = 1;
            } else if (Gdx.input.isKeyJustPressed(Keys.S)) {
                state = 0;
                dificulty = 2;
            }
        }

        if (state == 0) {
            batch.begin();
            startFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            startFont.draw(batch, "Tap to start!", 370, 240);

            batch.end();
            if (Gdx.input.justTouched() || (Gdx.input.isKeyJustPressed(Keys.SPACE))) {

                state = 1;
            }
        }

        if (state == 1) {
            if (Gdx.input.isKeyJustPressed(Keys.UP) || (Gdx.input.isKeyJustPressed(Keys.SPACE)) || Gdx.input.justTouched()) {
                velocity = -5;
                flap.play();
            }
            if (flapy.y <= -10) {
                flapy.y = -10;
                state = 2;
            } else if (flapy.y >= 450) {
                flapy.y = 450;
                state = 2;

            } else {
                velocity = velocity + gravity;
                flapy.y = flapy.y - velocity;
            }

            if (flapy.x < 0) flapy.x = 0;
            if (flapy.x > 800 - 64) flapy.x = 800 - 64;

            if (dificulty == 1) {
                speed = 200;
                if (TimeUtils.nanoTime() - lastSpawnTime > 2000000000) spawnTube();

            } else if (dificulty == 2) {

//RAM UP SPEED

                if (score <= 4) {
                    speed = 550;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 950000000) spawnTube();
                }

                if (score >= 5 && score <= 9) {
                    speed = 300;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 1400000000) spawnTube();
                }
                if (score >= 10 && score <= 14) {
                    speed = 400;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 1100000000) spawnTube();
                }
                if (score >= 15 && score <= 19) {
                    speed = 450;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 1000000000) spawnTube();
                }
                if (score >= 20 && score <= 24) {
                    speed = 500;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 900000000) spawnTube();
                }
                if (score >= 25 && score <= 35) {
                    speed = 550;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 950000000) spawnTube();
                }

                if (score == 35) {
                    Arrow = true;
                    speed = 550;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 350000000) {
                        spawnArrow();
                        spawnTube();
                        L += 1200;
                    }


                    kocka = rand.nextInt(180) + 150;
                }
                if (score >= 36 && score < 40) {
                    Arrow = false;
                    speed = 1200;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 250000000) ;

                }
                if (score >= 40) {
                    speed = 550;
                    if (TimeUtils.nanoTime() - lastSpawnTime > 950000000) spawnTube();
                }

            }

            //ARROW
            for (Iterator<Rectangle> arowIter = rarrow.iterator(); arowIter.hasNext(); ) {
                Rectangle arow = arowIter.next();
                if (TimeUtils.nanoTime() - lastSpawnTime > 250000000) arowIter.remove();

            }
            //COIN
            for (Iterator<Rectangle> coinIter = coins.iterator(); coinIter.hasNext(); ) {
                Rectangle coin = coinIter.next();
                coin.x -= speed * Gdx.graphics.getDeltaTime();
                if (coin.x < 0) coinIter.remove();
                if (coin.overlaps(flapy)) {
                    score++;
                    vypisScore = "score: " + score;
                    coinPickUp.play();
                    coinIter.remove();
                }
            }
            //TOP TUBE
            for (Iterator<Rectangle> tubeIter = toptube.iterator(); tubeIter.hasNext(); ) {
                Rectangle tube = tubeIter.next();
                tube.x -= speed * Gdx.graphics.getDeltaTime();
                if (tube.x < 0) tubeIter.remove();
                if (tube.overlaps(flapy)) {
                    oof.play();
                    state = 2;

                }
            }
            // BOT TUBE
            for (Iterator<Rectangle> btubeIter = bottube.iterator(); btubeIter.hasNext(); ) {
                Rectangle btube = btubeIter.next();
                btube.x -= speed * Gdx.graphics.getDeltaTime();
                if (btube.x < 0) btubeIter.remove();
                if (btube.overlaps(flapy)) {
                    oof.play();
                    state = 2;
                }
            }

        } else if (state == 2) {
            batch.begin();
            startFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            gOverFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            gOverFont.draw(batch, "Game Over!", 330, 260);
            startFont.draw(batch, "Press space to restart \n Press ESC to return", 350, 230);
            gOverFont.getData().setScale(2);

            batch.end();
            L = 2500;

            if (Gdx.input.justTouched() || (Gdx.input.isKeyJustPressed(Keys.SPACE))) {
                Death();

            }
            else if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
                app.setScreen(app.mainMenuScreen);
                Death();
            }
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
        coinsImage.dispose();
        topImage.dispose();
        botImage.dispose();
        batch.dispose();
        flappyImage.dispose();
        flap.dispose();
        oof.dispose();
        coinPickUp.dispose();

    }

}