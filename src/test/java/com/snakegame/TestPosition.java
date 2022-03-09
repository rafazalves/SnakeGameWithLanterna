package com.snakegame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPosition {
    Position p1;
    Position p2;

    @BeforeEach
    public void setup() {
        p1 = new Position(0,0);
        p2 = new Position(15,15);
    }

    @Test
    public void setX() {
        p1.setX(15);

        Assertions.assertEquals(15, p1.getX());
    }

    @Test
    public void setY() {
        p1.setY(15);

        Assertions.assertEquals(15, p1.getY());
    }

    @Test
    public void getInitialX() {
        Assertions.assertEquals(0, p1.getX());
    }

    @Test
    public void getInitialY() {
        Assertions.assertEquals(0, p1.getY());
    }

    @Test
    public void differentPositions() {
        Assertions.assertFalse(p1.equals(p2));
    }

    @Test
    public void equalPositions() {
        p1.setX(15);
        p1.setY(15);

        Assertions.assertTrue(p1.equals(p2));
    }
}
