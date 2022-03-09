package com.snakegame;

import java.util.LinkedList;
import java.util.Random;

public class Game {

	public Snake snake;

	public SnakeMultiplayer snakeMultiplayer;

	private LinkedList<Position> cerejas;

	private LinkedList<Position> cerejasbonus;

	private LinkedList<Position> obstaculos;

	private int score;
	private int scoremult;

	private final Random rand;

	private int MAX_SNAKE_SIZE;

	private boolean MULTIPLAYER;

	public Game(int max_snake_size, int initial_snake_size, boolean multiplayer) {
		rand = new Random();
		MAX_SNAKE_SIZE = max_snake_size;
		MULTIPLAYER = multiplayer;

		// Cria a Snake virada para a direita
		snake  = new Snake(Direction.RIGHT, initial_snake_size, 0);

		if(multiplayer) {
			snakeMultiplayer = new SnakeMultiplayer(Direction.LEFT, initial_snake_size, 80-3);
		}

		cerejas   = new LinkedList<Position>();
		cerejasbonus   = new LinkedList<Position>();
		obstaculos = new LinkedList<Position>();

		score  = 0;
		scoremult = 0;
	}

	// Lista com as cerejas
	public LinkedList<Position> getCerejas() {
		return cerejas;
	}
	// Lista com as cerejas bonus
	public LinkedList<Position> getCerejasBonus() {

		return cerejasbonus;
	}
	// Lista com os obstaculos
	public LinkedList<Position> getObstaculos() {
		return obstaculos;
	}

	// Score atual do jogo
	public int getScore() {
		return score;
	}
	public int getScoreMultiplayer() {
		return scoremult;
	}

	// Verifica se a Snake esta viva
	public boolean isSnakeAlive() {
		if(MULTIPLAYER){
			return snake.isAlive() && snakeMultiplayer.isAlive();
		}
		return snake.isAlive();
	}

	// Mata a Snake
	public void killSnake() {
		snake.kill();
	}
	public void killSnakeMultiplayer() {
		snakeMultiplayer.kill();
	}

	// Move a Snake na direcao atual
	public void moveSnake() {
		snake.move();
		if (snake.getCabeca().getX() == 80-1){
			snake.getCabeca().setX(0);
		}else if(snake.getCabeca().getX()==-1) {
			snake.getCabeca().setX(80-2);
		}else if(snake.getCabeca().getY()==23-1){
			snake.getCabeca().setY(0);
		}else if(snake.getCabeca().getY()==-1){
			snake.getCabeca().setY(23-2);
		}
	}
	public void moveSnakeMultiplayer() {
		snakeMultiplayer.move();
		if (snakeMultiplayer.getCabeca().getX() == 80-1){
			snakeMultiplayer.getCabeca().setX(0);
		}else if(snakeMultiplayer.getCabeca().getX()==-1) {
			snakeMultiplayer.getCabeca().setX(80-2);
		}else if(snakeMultiplayer.getCabeca().getY()==23-1){
			snakeMultiplayer.getCabeca().setY(0);
		}else if(snakeMultiplayer.getCabeca().getY()==-1){
			snakeMultiplayer.getCabeca().setY(23-2);
		}
	}

	public LinkedList<Position> getSnakeCorpo() {
		return snake.getCorpo();
	}
	public LinkedList<Position> getSnakeMultiplayerCorpo() {
		return snakeMultiplayer.getCorpo();
	}

	public Position getSnakeCabeca() {
		return snake.getCabeca();
	}
	public Position getSnakeMultiplayerCabeca() {
		return snakeMultiplayer.getCabeca();
	}

	public Position getSnakeCauda() {
		return snake.getCauda();
	}
	public Position getSnakeMultiplayerCauda() {
		return snakeMultiplayer.getCauda();
	}

	// Verifica se a Snake comeu uma cereja
	public boolean snakeEatCereja() {
		if(snake.eatCereja(cerejas)) {
			updateScore();
			if(snake.getSnakeSize() < MAX_SNAKE_SIZE) {
				snake.increaseSize();
			}

			return true;
		}

		return false;
	}
	public boolean snakeMultiplayerEatCereja() {
		if(snakeMultiplayer.eatCereja(cerejas)) {
			updateScoreMultiplayer();
			if(snakeMultiplayer.getSnakeSize() < MAX_SNAKE_SIZE) {
				snakeMultiplayer.increaseSize();
			}

			return true;
		}

		return false;
	}

	// Verifica se a Snake comeu uma cereja bonus
	public boolean snakeEatCerejaBonus() {
		if(snake.eatCerejaBonus(cerejasbonus)) {
			updateScore();
			updateScore();
			for(int i=0; i < 2; i++) {
				if(snake.getSnakeSize() < MAX_SNAKE_SIZE) {
					snake.increaseSize();
				}
			}

			return true;
		}

		return false;
	}
	public boolean snakeMultiplayerEatCerejaBonus() {
		if(snakeMultiplayer.eatCerejaBonus(cerejasbonus)) {
			updateScoreMultiplayer();
			updateScoreMultiplayer();
			for(int i=0; i < 2; i++) {
				if(snakeMultiplayer.getSnakeSize() < MAX_SNAKE_SIZE) {
					snakeMultiplayer.increaseSize();
				}
			}

			return true;
		}

		return false;
	}

	// Verifica se a Snake embateu num obstaculo
	public boolean snakeColisaoObstaculo() {
		if(snake.colisaoObstaculo(obstaculos)) {
			return true;
		}

		return false;
	}
	public boolean snakeMultiplayerColisaoObstaculo() {
		if(snakeMultiplayer.colisaoObstaculo(obstaculos)) {
			return true;
		}

		return false;
	}

	// Muda a direcao da Snake impedindo-a de mudar na direcao oposta a direcao atual
	public void setDirection(Direction dir) {
		switch(dir) {
			case UP:

				if(snake.getDirection() != Direction.DOWN)
					snake.setDirection(Direction.UP);

				break;

			case DOWN:

				if(snake.getDirection() != Direction.UP)
					snake.setDirection(Direction.DOWN);

				break;

			case LEFT:

				if(snake.getDirection() != Direction.RIGHT)
					snake.setDirection(Direction.LEFT);

				break;

			case RIGHT:

				if(snake.getDirection() != Direction.LEFT)
					snake.setDirection(Direction.RIGHT);

				break;

			default:
				throw new IllegalArgumentException("Não existe essa direção");
		}
	}
	public void setDirectionMultiplayer(Direction dir) {
		switch(dir) {
			case UP:

				if(snakeMultiplayer.getDirection() != Direction.DOWN)
					snakeMultiplayer.setDirection(Direction.UP);

				break;

			case DOWN:

				if(snakeMultiplayer.getDirection() != Direction.UP)
					snakeMultiplayer.setDirection(Direction.DOWN);

				break;

			case LEFT:

				if(snakeMultiplayer.getDirection() != Direction.RIGHT)
					snakeMultiplayer.setDirection(Direction.LEFT);

				break;

			case RIGHT:

				if(snakeMultiplayer.getDirection() != Direction.LEFT)
					snakeMultiplayer.setDirection(Direction.RIGHT);

				break;

			default:
				throw new IllegalArgumentException("Não existe essa direção");
		}
	}

	// Atualiza o Score atual do jogo
	public void updateScore() {
		score += 1;
	}
	public void updateScoreMultiplayer() {
		scoremult += 1;
	}

	private int randomNumber(int min, int max) {
		return rand.nextInt(max - min) + min;
	}


	private Position generateRandomPosition(int max_x, int max_y) {
		int x = randomNumber(1, max_x);
		int y = randomNumber(1, max_y);

		return new Position(x, y);
	}

	// Verifica se a posicao p está vazia
	public boolean isEmptyPosition(Position p) {
		if(MULTIPLAYER){
			return !(snake.isCorpo(p) || cerejas.contains(p) || obstaculos.contains(p) || snakeMultiplayer.isCorpo(p));
		}
		return !(snake.isCorpo(p) || cerejas.contains(p) || obstaculos.contains(p));
	}

	// Atribui uma nova posicao a um objeto que nao esteja atualmente ocupada
	public Position generateRandomObject(int x_max, int y_max) {
		Position p = null;

		do {
			p = generateRandomPosition(x_max, y_max);

		} while(!isEmptyPosition(p));

		return p;
	}
}