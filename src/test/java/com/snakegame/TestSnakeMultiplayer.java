package com.snakegame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class TestSnakeMultiplayer {
    private SnakeMultiplayer snakemult;

    @BeforeEach
    public void setUp() {
        snakemult = new SnakeMultiplayer(Direction.LEFT, 1, 77);
    }

    @Test
    public void tamanhoCorpo() {
        Assertions.assertEquals(1, snakemult.getCorpo().size());
    }

    @Test
    public void positionCabeca() {
        Position p = new Position(78,15);

        Assertions.assertEquals(p,snakemult.getCabeca());
    }

    @Test
    public void tamanhoInicialSnake() {
        Assertions.assertEquals(1, snakemult.getSnakeSize());
    }

    @Test
    public void mudarDirection() {
        snakemult.setDirection(Direction.DOWN);

        Assertions.assertEquals(Direction.DOWN, snakemult.getDirection());
    }

    @Test
    public void snakeInitialDirection() {
        Assertions.assertEquals(Direction.LEFT, snakemult.getDirection());
    }

    @Test
    public void snakeIsAlive() {
        Assertions.assertTrue(snakemult.isAlive());
    }

    @Test
    public void killSnake() {
        snakemult.kill();

        Assertions.assertFalse(snakemult.isAlive());
    }

    @Test
    public void isCorpo() {
        Position p = new Position(78, 15);

        Assertions.assertTrue(snakemult.isCorpo(p));
    }

    @Test
    public void eatCereja() {
        Position p = new Position(78, 15);
        LinkedList<Position> cerejas = new LinkedList<>();
        cerejas.addLast(p);

        Assertions.assertTrue(snakemult.eatCereja(cerejas));
    }

    @Test
    public void eatCerejaBonus() {
        Position p = new Position(78, 15);
        LinkedList<Position> cerejasbonus = new LinkedList<>();
        cerejasbonus.addLast(p);

        Assertions.assertTrue(snakemult.eatCerejaBonus(cerejasbonus));
    }

    @Test
    public void collideObstaculo() {
        Position p = new Position(78, 15);
        LinkedList<Position> obstaculos = new LinkedList<>();
        obstaculos.addLast(p);

        Assertions.assertTrue(snakemult.colisaoObstaculo(obstaculos));
    }

    @Test
    public void increaseSize() {
        snakemult.increaseSize();

        Assertions.assertEquals(2, snakemult.getSnakeSize());
    }
}
