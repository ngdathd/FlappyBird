package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.common.GameState;
import com.ngdathd.flappybird.helpers.AssetLoader;
import com.ngdathd.flappybird.interfaces.GameStateListener;

public class Bird extends Actor {
    private static final float IMG_BIRD_WIDTH = 34f;
    private static final float IMG_BIRD_HEIGHT = 24f;
    private static final float GRAVITY = 10f;   // Gia tốc trọng trường

    private final Skin skin;
    private final String[] birdColors;

    private final float worldWidth;
    private final float worldHeight;
    private final float initialPositionX;
    private final float initialPositionY;
    private final float accelerationSpeed;

    private final Vector2 position;
    private final Vector2 velocity;  // Tốc độ của Bird
    private final Vector2 acceleration;  // Gia tốc của Bird
    private final Circle birdCircle;
    private Animation<TextureRegion> flapAnimation;

    private float rotation;
    private GameState gameState;
    private float stateTime;
    private float elapsedTime;
    private boolean movingUp;

    private final Sound soundWing;
    private final Sound soundHit;
    private final Sound soundDie;

    private Land land;
    private Pipe pipe1;
    private Pipe pipe2;
    private Pipe pipe3;
    private GameStateListener gameStateListener;

    public Bird(Stage stage) {
        skin = AssetLoader.INSTANCE.getSkin();
        birdColors = new String[]{"red", "orange", "blue"};

        worldWidth = stage.getViewport().getWorldWidth();
        worldHeight = stage.getViewport().getWorldHeight();

        // Tính toán kích thước cho Bird
        float widthBird = IMG_BIRD_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        float heightBird = widthBird * IMG_BIRD_HEIGHT / IMG_BIRD_WIDTH;
        setSize(widthBird, heightBird);

        // Khởi tạo Circle cho Bird để kiểm tra va chạm
        birdCircle = new Circle();
        birdCircle.setRadius(getWidth() / 2f - 2f * worldWidth / Constants.BASELINE_WORLD_WIDTH);

        // Tính toán vị trí ban đầu cho Bird
        initialPositionX = worldWidth / 3f - widthBird / 2f - 2f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        initialPositionY = worldHeight / 2f - heightBird / 2f - 2f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        position = new Vector2(initialPositionX, initialPositionY);
        setPosition(position.x, position.y);
        birdCircle.setPosition(position.x + getWidth() / 2f, position.y + getHeight() / 2f);

        accelerationSpeed = Constants.SCROLL_SPEED * GRAVITY * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        // Khởi tạo các tốc độ và gia tốc cho Bird
        velocity = new Vector2(0f, 0f);
        acceleration = new Vector2(0f, accelerationSpeed);

        // Đặt tâm quay của Bird ở trung tâm
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        // Đặt góc xoay cho Bird
        setRotation(0f);

        gameState = GameState.READY;
        stateTime = 0f;
        elapsedTime = 0f;
        movingUp = true;

        soundWing = AssetLoader.INSTANCE.getSound(AssetLoader.SOUND_WING);
        soundHit = AssetLoader.INSTANCE.getSound(AssetLoader.SOUND_HIT);
        soundDie = AssetLoader.INSTANCE.getSound(AssetLoader.SOUND_DIE);
    }

    public void setupBird(Land land, Pipe pipe1, Pipe pipe2, Pipe pipe3, GameStateListener gameStateListener) {
        this.land = land;
        this.pipe1 = pipe1;
        this.pipe2 = pipe2;
        this.pipe3 = pipe3;
        this.gameStateListener = gameStateListener;
    }

    public void resetBird() {
        // Lấy màu ngẫu nhiên cho Bird
        String selectedColor = birdColors[MathUtils.random(birdColors.length - 1)];
        TextureRegion[] flapFrames = new TextureRegion[3];
        flapFrames[0] = skin.getRegion("bird-" + selectedColor + "-1");
        flapFrames[1] = skin.getRegion("bird-" + selectedColor + "-2");
        flapFrames[2] = skin.getRegion("bird-" + selectedColor + "-3");
        flapAnimation = new Animation<>(0.15f, flapFrames);
        flapAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Đặt lại vị trí ban đầu cho Bird
        position.set(initialPositionX, initialPositionY);
        setPosition(position.x, position.y);
        birdCircle.setPosition(position.x + getWidth() / 2f, position.y + getHeight() / 2f);

        // Đặt lại tốc độ và gia tốc cho Bird
        velocity.set(0f, 0f);
        acceleration.set(0f, accelerationSpeed);

        // Đặt lại góc xoay cho Bird
        setRotation(0f);

        // Đặt lại gameState, stateTime, elapsedTime, movingUp
        gameState = GameState.READY;
        stateTime = 0f;
        elapsedTime = 0f;
        movingUp = true;
    }

    public void fly() {
        gameState = GameState.PLAYING;
        velocity.y = 140f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        soundWing.play();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame;
        if (getRotation() == -90f) {
            // Nếu Bird đang cắm đầu xuống lấy khung hình thứ hai
            currentFrame = flapAnimation.getKeyFrames()[1];
        } else {
            // Nếu không, lấy khung hình từ animation
            currentFrame = flapAnimation.getKeyFrame(stateTime, true);
        }

        batch.draw(
            currentFrame,
            getX(), getY(),
            getOriginX(), getOriginY(),
            getWidth(), getHeight(),
            getScaleX(), getScaleY(),
            getRotation()
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (gameState == GameState.READY) {
            // Biên độ di chuyển
            float amplitude = 4f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
            // Thời gian di chuyển lên hoặc xuống
            float moveDuration = 0.3f;

            // Cập nhật thời gian di chuyển
            elapsedTime += delta;

            if (elapsedTime >= moveDuration) {
                elapsedTime = 0f;
                movingUp = !movingUp; // Đảo chiều di chuyển
            }

            // Tính toán vị trí di chuyển dựa trên chiều hiện tại
            float moveY = movingUp ? amplitude : -amplitude;
            position.y += moveY * delta / moveDuration;

            // Cập nhật vị trí của Bird từ position
            setPosition(position.x, position.y);
            birdCircle.setPosition(position.x + getWidth() / 2f, position.y + getHeight() / 2f);
        } else if (gameState == GameState.PLAYING) {
            /*
             * Thêm gia tốc và tốc độ vào tọa độ y của Bird, từ đó di chuyển lên xuống
             *
             * Ví dụ, Bird có các thuộc tính sau:
             *   - acceleration (gia tốc) ban đầu bằng (0, -460)
             *   - velocity (tốc độ) ban đầu bằng (0, 0)
             *   - position (vị trí) ban đầu bằng (100, 200)
             *
             * 1. Tính toán tại khung hình đầu tiên:
             *   - acceleration.cpy() tạo ra một bản sao của (0, -460).
             *   - .scl(delta) sẽ nhân bản sao này với delta = 0.016:
             *       (0, −460) * 0.016 = (0, −7.36)
             *   - velocity.add(...) thêm vector (0, -7.36) vào vector vận tốc hiện tại (0, 0):
             *       (0, 0) + (0, −7.36) = (0, −7.36)
             * => Lúc này, velocity = (0, −7.36)
             *   - velocity.cpy() tạo ra một bản sao của (0, −7.36)
             *   - .scl(delta) sẽ nhân bản sao này với delta = 0.016:
             *       (0, −7.36) * 0.016 = (0, −0.11776)
             *   - position.add(...) thêm vector (0, −0.11776) vào vector vị trí hiện tại (100, 200):
             *       (100, 200) + (0, −0.11776) = (100, 199.88224)
             * => Lúc này, position = (100, 199.88224)
             * => Bird đã di chuyển xuống dưới một đoạn bằng 0.11776 (Hệ toạ độ yDown)
             *
             * 2. Tính toán tại khung hình thứ hai:
             *   - acceleration vẫn bằng (0, -460) vì acceleration.cpy() đã tạo ra bản sao và bản sao
             * này nhân với delta, còn acceleration vẫn không thay đổi
             * => acceleration.cpy().scl(delta) vẫn bằng (0, -7.36)
             *   - velocity.add(...) thêm vector (0, −7.36) vào vector vận tốc hiện tại, lúc này đang
             * bằng (0, −7.36), vì velocity.cpy() tạo bản sao nên velocity không đổi dù đã nhân với delta:
             *       (0, −7.36) + (0, −7.36) = (0, −14.72)
             * => Lúc này, velocity = (0, −14.72)
             *   - velocity.cpy().scl(delta) tạo ra bản sao của (0, −14.72) và nhân với delta = 0.016:
             *       (0, −14.72) * 0.016 = (0, −0.23552)
             *   - position.add(...) thêm vector (0, −0.11776) vào vector vị trí hiện tại (100, 199.88224):
             *       (100, 199.88224) + (0, −0.23552) = (100, 199.64672)
             *
             * => Lúc này, position = (100, 199.64672)
             * => Bird đã di chuyển xuống dưới một đoạn bằng 0.23552 (Hệ toạ độ yDown)
             *
             * */
            velocity.add(acceleration.cpy().scl(delta));
            // Giới hạn tốc độ của Bird
            if (velocity.y > 200f * worldWidth / Constants.BASELINE_WORLD_WIDTH) {
                velocity.y = 200f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
            }
            // Kiểm tra giới hạn chiều cao
            if (position.y > worldHeight) {
                position.y = worldHeight;  // Giữ Bird không bay cao hơn worldHeight
                velocity.y = 0;  // Ngăn Bird tiếp tục di chuyển lên
            }
            position.add(velocity.cpy().scl(delta));

            // Cập nhật vị trí của Bird từ position
            setPosition(position.x, position.y);
            birdCircle.setPosition(position.x + getWidth() / 2f, position.y + getHeight() / 2f);

            // Xử lý xoay Bird: xoay Bird ngẩng lên 20 độ khi đang bay lên
            if (velocity.y > 0f) {
                rotation += 600f * delta;
                // Giới hạn góc xoay bằng 20 độ, Bird chỉ ngẩng đầu lên tối đa 20 độ
                if (rotation > 20f) {
                    rotation = 20f;
                }
            }
            // Xử lý xoay Bird: xoay Bird cúi xuống khi đang rơi
            if (isFalling()) {
                rotation -= 480f * delta;
                // Nếu Bird đang rơi xuống, giới hạn góc xoay bằng -90 độ, Bird cắm đầu xuống
                if (rotation < -90f) {
                    rotation = -90f;
                }
            }
            setRotation(rotation);

            // Kiểm tra va chạm giữa Bird với Land và các Pipe
            if (isCollidingWithLand()
                || isCollidingWithPipe(pipe1)
                || isCollidingWithPipe(pipe2)
                || isCollidingWithPipe(pipe3)
            ) {
                gameState = GameState.GAME_OVER;
                gameStateListener.onGameStateGameOver();
                soundHit.play();
                soundDie.play();
            }

            // Tính điểm khi vượt qua Pipe
            scoreWithPipe(pipe1);
            scoreWithPipe(pipe2);
            scoreWithPipe(pipe3);
        } else if (gameState == GameState.GAME_OVER) {
            if (velocity.y != 0f) {
                // Nếu Bird đã va chạm nhưng chưa dừng lại
                // Kiểm tra va chạm với mặt đất
                if (isCollidingWithLand()) {
                    // Nếu Bird đã va chạm với mặt đất, đặt vận tốc Bird bằng 0
                    velocity.set(0f, 0f);
                } else {
                    velocity.add(acceleration.cpy().scl(delta));
                }
                position.add(velocity.cpy().scl(delta));

                // Cập nhật vị trí của Bird từ position
                setPosition(position.x, position.y);
                birdCircle.setPosition(position.x + getWidth() / 2f, position.y + getHeight() / 2f);

                // Xử lý xoay Bird: xoay Bird để nó rơi vuông góc với mặt đất
                setRotation(-90f);
            }
        }
    }

    private boolean isFalling() {
        return velocity.y < -110f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
    }

    private boolean isCollidingWithLand() {
        float yLine = land.getCollideY();
        return (birdCircle.y - birdCircle.radius) <= yLine;
    }

    private boolean isCollidingWithPipe(Pipe pipe) {
        return Intersector.overlaps(birdCircle, pipe.getBoundingRectangleUp())
            || Intersector.overlaps(birdCircle, pipe.getBoundingRectangleDown());
    }

    private void scoreWithPipe(Pipe pipe) {
        if (!pipe.isScored() && birdCircle.x > pipe.getScoreX()) {
            pipe.setScored(true);
            gameStateListener.onGameStateScore();
        }
    }
}
