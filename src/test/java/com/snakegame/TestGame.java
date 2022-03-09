package com.snakegame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGame {
    private Game game;
    private Game gamemultiplayer;

    @BeforeEach
    public void setUp() {
        game = new Game(20, 1, false);
        gamemultiplayer = new Game(20,1,true);
    }

    @Test
    public void getCerejasSize() {
        Assertions.assertEquals(0, game.getCerejas().size());
    }

    @Test
    public void getCerejasBonusSize() {
        Assertions.assertEquals(0, game.getCerejasBonus().size());
    }

    @Test
    public void getObstaculosSize() {
        Assertions.assertEquals(0, game.getObstaculos().size());
    }

    @Test
    public void initialScore() {
        Assertions.assertEquals(0, game.getScore());
    }

    @Test
    public void initialScoreMultiplayer() {
        Assertions.assertEquals(0, gamemultiplayer.getScoreMultiplayer());
    }

    @Test
    public void isSnakeAlive() {
        Assertions.assertTrue(game.isSnakeAlive());
    }

    @Test
    public void snakeKill() {
        game.killSnake();

        Assertions.assertFalse(game.isSnakeAlive());
    }

    @Test
    public void snakeMultiplayerKill() {
        gamemultiplayer.killSnakeMultiplayer();

        Assertions.assertFalse(gamemultiplayer.isSnakeAlive());
    }

    @Test
    public void snakeCorpoSize() {
        Assertions.assertEquals(1, game.getSnakeCorpo().size());
    }

    @Test
    public void snakeMultiplayerCorpoSize() {
        Assertions.assertEquals(1, gamemultiplayer.getSnakeMultiplayerCorpo().size());
    }

    @Test
    public void snakeCabecaPosition() {
        Position p = new Position(1,15);

        Assertions.assertEquals(p, game.getSnakeCabeca());
    }

    @Test
    public void snakeMultiplayerCabecaPosition() {
        Position p = new Position(78,15);

        Assertions.assertEquals(p, gamemultiplayer.getSnakeMultiplayerCabeca());
    }

    @Test
    public void snakeColisaoObstaculo() {
        Position p = new Position(1, 15);
        game.getObstaculos().addLast(p);


        Assertions.assertTrue(game.snakeColisaoObstaculo());
    }

    @Test
    public void snakeMultiplayerColisaoObstaculo() {
        Position p = new Position(78, 15);
        gamemultiplayer.getObstaculos().addLast(p);

        Assertions.assertTrue(gamemultiplayer.snakeMultiplayerColisaoObstaculo());
    }

    @Test
    public void setSnakeDirection() {
        game.setDirection(Direction.UP);

        Assertions.assertEquals(Direction.UP, game.snake.getDirection());
    }

    @Test
    public void setSnakeMultiplayerDirection() {
        gamemultiplayer.setDirectionMultiplayer(Direction.DOWN);

        Assertions.assertEquals(Direction.DOWN, gamemultiplayer.snakeMultiplayer.getDirection());
    }

    @Test
    public void updateScore() {
        game.updateScore();

        Assertions.assertEquals(1, game.getScore());
    }

    @Test
    public void updateScoreMultiplayer() {
        gamemultiplayer.updateScoreMultiplayer();

        Assertions.assertEquals(1, gamemultiplayer.getScoreMultiplayer());
    }

    @Test
    public void isEmptyPosition() {
        Position p = new Position(1,15);

        Assertions.assertFalse(game.isEmptyPosition(p));
    }

    @Test
    public void eatCerejaUpdatesScore() {
        Position p = new Position(1,15);
        game.getCerejas().addLast(p);

        game.snakeEatCereja();

        Assertions.assertEquals(1, game.getScore());
    }

    @Test
    public void eatCerejaBonusUpdatesScore() {
        Position p = new Position(1,15);
        game.getCerejasBonus().addLast(p);

        game.snakeEatCerejaBonus();

        Assertions.assertEquals(2, game.getScore());
    }

    @Test
    public void eatCerejaMultiplayerUpdatesScore() {
        Position p = new Position(78,15);
        gamemultiplayer.getCerejas().addLast(p);

        gamemultiplayer.snakeMultiplayerEatCereja();

        Assertions.assertEquals(1, gamemultiplayer.getScoreMultiplayer());
    }

    @Test
    public void eatCerejaBonusMultiplayerUpdatesScore() {
        Position p = new Position(78,15);
        gamemultiplayer.getCerejasBonus().addLast(p);

        gamemultiplayer.snakeMultiplayerEatCerejaBonus();

        Assertions.assertEquals(2, gamemultiplayer.getScoreMultiplayer());
    }
}
