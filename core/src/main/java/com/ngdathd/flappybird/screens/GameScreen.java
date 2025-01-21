package com.ngdathd.flappybird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ngdathd.flappybird.actors.Background;
import com.ngdathd.flappybird.actors.Bird;
import com.ngdathd.flappybird.actors.HighScorePanel;
import com.ngdathd.flappybird.actors.Land;
import com.ngdathd.flappybird.actors.Pipe;
import com.ngdathd.flappybird.actors.Score;
import com.ngdathd.flappybird.actors.ScorePanel;
import com.ngdathd.flappybird.actors.Tutorial;
import com.ngdathd.flappybird.base.AndroidInterface;
import com.ngdathd.flappybird.base.BaseScreen;
import com.ngdathd.flappybird.base.IosInterface;
import com.ngdathd.flappybird.common.GameState;
import com.ngdathd.flappybird.interfaces.GameStateListener;
import com.ngdathd.flappybird.interfaces.OnClickScorePanelListener;

public class GameScreen extends BaseScreen implements OnClickScorePanelListener {
    private final Background background;
    private final Land land;
    private final Pipe pipe1;
    private final Pipe pipe2;
    private final Pipe pipe3;
    private final Score score;
    private final Bird bird;
    private final Tutorial tutorial;
    private final ScorePanel scorePanel;
    private final HighScorePanel highScorePanel;

    private GameState gameState;

    public GameScreen(AndroidInterface androidInterface, IosInterface iosInterface) {
        background = new Background(stage, androidInterface, iosInterface);
        land = new Land(stage);
        pipe1 = new Pipe(stage, land, 0);
        pipe2 = new Pipe(stage, land, 1);
        pipe3 = new Pipe(stage, land, 2);
        score = new Score(stage);
        bird = new Bird(stage);
        tutorial = new Tutorial(stage);
        scorePanel = new ScorePanel(stage, this);
        highScorePanel = new HighScorePanel(stage);

        gameState = GameState.READY;

        bird.setupBird(land, pipe1, pipe2, pipe3, new GameStateListener() {
            @Override
            public void onGameStateScore() {
                score.handleScore();
            }

            @Override
            public void onGameStateGameOver() {
                pipe1.stopPipeRun();
                pipe2.stopPipeRun();
                pipe3.stopPipeRun();
                land.stopLandRun();
                scorePanel.showScorePanel(score.getScore());

                gameState = GameState.GAME_OVER;
            }
        });

        stage.addActor(background);
        stage.addActor(pipe1);
        stage.addActor(pipe2);
        stage.addActor(pipe3);
        stage.addActor(land);
        stage.addActor(bird);
        stage.addActor(score);
        stage.addActor(tutorial);
        stage.addActor(scorePanel);
        stage.addActor(highScorePanel);

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameState == GameState.READY) {
                    tutorial.hideTutorial();
                    pipe1.startPipeRun();
                    pipe2.startPipeRun();
                    pipe3.startPipeRun();
                    bird.fly();

                    gameState = GameState.PLAYING;
                } else if (gameState == GameState.PLAYING) {
                    bird.fly();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();

        startGame();
    }

    private void startGame() {
        pipe1.resetPipe();
        pipe2.resetPipe();
        pipe3.resetPipe();
        score.resetScore();
        background.randomizeBackground();
        bird.resetBird();
        tutorial.showTutorial();
        land.startLandRun();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void onButtonPlayClicked() {
        stage.addAction(Actions.sequence(
            Actions.run(() -> {
                scorePanel.hideScorePanel();
                if (highScorePanel.getColor().a != 0f) {
                    highScorePanel.hideHighScorePanelMoveTo();
                }
            }),
            Actions.delay(1.1f),
            Actions.run(this::startGame),
            Actions.delay(0.3f),
            Actions.run(() -> gameState = GameState.READY)
        ));
    }

    @Override
    public void onButtonRankClicked() {
        if (highScorePanel.getColor().a == 0f) {
            highScorePanel.showHighScorePanel();
        } else {
            highScorePanel.hideHighScorePanelFadeOut();
        }
    }
}
