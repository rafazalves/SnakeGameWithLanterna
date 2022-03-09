package com.snakegame;

public class Application {

    // Tamanho do terminal
    private final static int LARGE_WIDTH  = 80;
    private final static int LARGE_HEIGHT = 23;

    public static void main(String[] args) {
        GameBuild snake_game = new GameBuild(LARGE_WIDTH, LARGE_HEIGHT);
        snake_game.openMainMenu();
    }
}
