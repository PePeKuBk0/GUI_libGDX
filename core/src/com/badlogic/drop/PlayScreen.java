package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import java.util.Iterator;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Random;



public class PlayScreen implements Screen {

    private Texture birdObstacleImage;
    private Texture cactusObstacleImage;
    private Texture dinoImage;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Rectangle dino;
    private Array<Rectangle> cactusObstacles;
    private Array<Rectangle> birdObstacles;
    private long lastObstacleSpawnTime;
    float velocity = 0.0f;
    float gravity = 0.6f;
    boolean highestPointReached = false;
    Random rand = new Random();
    private int score;
    private int highScore;
    private String vypisScore;
    private String deathScreenText;
    private String vypisHighScore;
    BitmapFont scoreFont;
    BitmapFont deathScreenFont;
    int obstacleSpeed = 300;
    int timeToSpeed = 2000;
    boolean isDead = false;
    private Skin skin;
    private Music music;
    private Sound jumpDino;
    private Sound AAAAA;

    private final Application app;
    private Stage stage;

    public PlayScreen(final Application app){
        this.app = app;
        this.stage = new Stage(new StretchViewport(Application.V_WIDHT, Application.V_HEIGHT, app.camera));
    }


    @Override
    public void show() {
        cactusObstacleImage = new Texture(Gdx.files.internal("cactus.png"));
        birdObstacleImage = new Texture(Gdx.files.internal("birdObstacle.png"));
        dinoImage = new Texture(Gdx.files.internal("dino.png"));


        score = 0;
        vypisScore = "score: 0";
        vypisHighScore = "hi-score: 0";
        deathScreenText = "       GAME OVER \npress spacebar to retry \n  press ESC to return";

        scoreFont = new BitmapFont();
        deathScreenFont = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        Music music = Gdx.audio.newMusic(Gdx.files.internal("MainMusic.mp3"));
        music.setVolume(0f);
        music.setLooping(true);
        music.play();
        jumpDino = Gdx.audio.newSound(Gdx.files.internal("jumpDino.mp3"));
        AAAAA = Gdx.audio.newSound(Gdx.files.internal("AAAAA.mp3"));
        dino = new Rectangle();
        dino.x = -64;
        dino.y = 20;
        dino.width = 64;
        dino.height = 64;

        cactusObstacles = new Array<Rectangle>();
        birdObstacles = new Array<Rectangle>();
        spawnCactusObstacle();

        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font);
        this.skin.load((Gdx.files.internal("uiskin.json")));
    }
    private void spawnBirdObstacle() {
        Rectangle bird = new Rectangle();
        bird.y = 220;
        bird.x = 800 - 64;
        bird.width = 64;
        bird.height = 48;
        birdObstacles.add(bird);
        lastObstacleSpawnTime = TimeUtils.millis();
    }

    private void spawnCactusObstacle() {
        Rectangle cactus = new Rectangle();
        cactus.y = 20;
        cactus.x = 800 - 64;
        cactus.width = 48;
        cactus.height = 65;
        cactusObstacles.add(cactus);
        lastObstacleSpawnTime = TimeUtils.millis();
    }

    private void resetOnDeath(){
        isDead = false;
        score = 0;
        dino.y=20;
        obstacleSpeed = 300;
        timeToSpeed = 2000;

        for (Iterator<Rectangle> birdIter = birdObstacles.iterator(); birdIter.hasNext(); ) {   //vymaze vsetky kaktusy a vtaky ked stlacim medzernik
            Rectangle bird = birdIter.next();
            if(bird.x > 0) birdIter.remove();
        }

        for (Iterator<Rectangle> cactusIter = cactusObstacles.iterator(); cactusIter.hasNext(); ) {
            Rectangle cactus = cactusIter.next();
            if(cactus.x >0) cactusIter.remove();
        }
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(1.0f, 1.0f, 1.0f, 1f);
        Preferences prefs = Gdx.app.getPreferences("My Preferences");

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        ShapeRenderer zem = new ShapeRenderer();
        zem.setColor(Color.BLACK);
        zem.begin(ShapeType.Line);
        zem.line(0, 30, 860, 30);
        zem.end();

        batch.begin();

        scoreFont.setColor(0f, 0f, 0f, 1.0f);
        deathScreenFont.setColor(0f, 0f, 0f, 1.0f);
        deathScreenFont.getData().setScale(2);

        scoreFont.draw(batch, vypisScore, 10, 470);
        scoreFont.draw(batch, vypisHighScore, 700, 470);

        if (isDead) {
            deathScreenFont.draw(batch, deathScreenText, 250, 300);
        }
        Sprite dinoSize = new Sprite(dinoImage);
        dinoSize.setScale(1f);
        dinoSize.setPosition(dino.x, dino.y);
        dinoSize.setSize(dino.width, dino.height);
        dinoSize.draw(batch);


        for(Rectangle cactus: cactusObstacles) {
            Sprite cactusSize = new Sprite(cactusObstacleImage);
            cactusSize.setScale(1f);
            cactusSize.setPosition(cactus.x, cactus.y);
            cactusSize.setSize(cactus.width, cactus.height);
            cactusSize.draw(batch);
        }


        for(Rectangle bird: birdObstacles) {

            Sprite birdSize = new Sprite(birdObstacleImage);
            birdSize.setScale(1f);
            birdSize.setPosition(bird.x, bird.y);
            birdSize.setSize(bird.width, bird.height);
            birdSize.draw(batch);
        }



        batch.end();


        if(!highestPointReached && !isDead){
            if((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.SPACE)) && dino.y <= 20 && !Gdx.input.isKeyPressed(Keys.DOWN)){
                velocity = -15;
                jumpDino.play(.2f);
            }
            if(Gdx.input.isKeyPressed(Keys.DOWN) && dino.y >50 && !Gdx.input.isKeyPressed(Keys.UP)){
                velocity = 15;
            }
            if(dino.y < 20){
                dino.y = 20;
            }
            else{
                velocity = velocity + gravity;
                dino.y = dino.y-velocity;
            }
            if(dino.y > 220){
                highestPointReached = true;
            }
        }
        if(highestPointReached){
            if(dino.y < 20){
                velocity = 15;
            }
            else{
                velocity = velocity + gravity;
                dino.y = dino.y-velocity;
            }
            if(dino.y < 20){
                highestPointReached = false;
            }
        }

        if(dino.x < 0) dino.x = 0;
        if(dino.y < 20) dino.y = 20;


        if(!isDead){

            if(TimeUtils.millis() - lastObstacleSpawnTime > timeToSpeed){
                int randomObstacleSpawn = rand.nextInt(2);
                if(randomObstacleSpawn == 0){
                    spawnCactusObstacle();
                }
                else if(randomObstacleSpawn == 1){
                    spawnBirdObstacle();
                }
                if(obstacleSpeed <1250){
                    obstacleSpeed+=20;
                    timeToSpeed -= 30;
                }
            }


            score++;
            vypisScore = "score: " + score;
            highScore = prefs.getInteger("highscore");
            if(highScore < score){
                highScore = score;
                prefs.putInteger("highscore", highScore);
                prefs.flush();
            }
            vypisHighScore = "hi-score: " + highScore;


            for (Iterator<Rectangle> iter = cactusObstacles.iterator(); iter.hasNext(); ) {
                Rectangle cactus = iter.next();
                cactus.x -= obstacleSpeed * Gdx.graphics.getDeltaTime();
                if(cactus.x < 0 - 80) iter.remove();
                if(cactus.overlaps(dino)) {
                    isDead = true;
                    AAAAA.play(2f);
                }
            }

            for (Iterator<Rectangle> birdIter = birdObstacles.iterator(); birdIter.hasNext(); ) {
                Rectangle bird = birdIter.next();
                bird.x -= obstacleSpeed * Gdx.graphics.getDeltaTime();
                if(bird.x < 0 - 80) birdIter.remove();
                if(bird.overlaps(dino)) {
                    isDead = true;
                    AAAAA.play(2f);
                }
            }

        }
        else{
            if(Gdx.input.isKeyPressed(Keys.SPACE)){
                resetOnDeath();
            }
            else if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
                app.setScreen(app.mainMenuScreen);
                resetOnDeath();
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
        cactusObstacleImage.dispose();
        birdObstacleImage.dispose();
        dinoImage.dispose();
        batch.dispose();
        jumpDino.dispose();
    }
}
