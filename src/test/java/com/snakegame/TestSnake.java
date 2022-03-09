package com.snakegame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class TestSnake {
    private Snake snake;

    @BeforeEach
    public void setUp() {
        snake = new Snake(Direction.RIGHT, 1, 0);
    }

    @Test
    public void tamanhoCorpo() {
        Assertions.assertEquals(1, snake.getCorpo().size());
    }

    @Test
    public void positionCabeca() {
        Position p = new Position(1,15);

        Assertions.assertEquals(p,snake.getCabeca());
    }

    @Test
    public void tamanhoInicialSnake() {
        Assertions.assertEquals(1, snake.getSnakeSize());
    }

    @Test
    public void mudarDirection() {
        snake.setDirection(Direction.UP);

        Assertions.assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    public void snakeInitialDirection() {
        Assertions.assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    public void snakeIsAlive() {
        Assertions.assertTrue(snake.isAlive());
    }

    @Test
    public void killSnake() {
        snake.kill();

        Assertions.assertFalse(snake.isAlive());
    }

    @Test
    public void isCorpo() {
        Position p = new Position(1, 15);

        Assertions.assertTrue(snake.isCorpo(p));
    }

    @Test
    public void eatCereja() {
        Position p = new Position(1, 15);
        LinkedList<Position> cerejas = new LinkedList<>();
        cerejas.addLast(p);

        Assertions.assertTrue(snake.eatCereja(cerejas));
    }

    @Test
    public void eatCerejaBonus() {
        Position p = new Position(1, 15);
        LinkedList<Position> cerejasbonus = new LinkedList<>();
        cerejasbonus.addLast(p);

        Assertions.assertTrue(snake.eatCerejaBonus(cerejasbonus));
    }

    @Test
    public void collideObstaculo() {
        Position p = new Position(1, 15);
        LinkedList<Position> obstaculos = new LinkedList<>();
        obstaculos.addLast(p);

        Assertions.assertTrue(snake.colisaoObstaculo(obstaculos));
    }

    @Test
    public void increaseSize() {
        snake.increaseSize();

        Assertions.assertEquals(2, snake.getSnakeSize());
    }
}
