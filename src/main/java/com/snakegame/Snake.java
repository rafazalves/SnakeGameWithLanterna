package com.snakegame;

import java.util.LinkedList;

// Representacao da Snake com a sua cabeça, corpo e a direcao que se esta a mover
public class Snake {

	// Define a direcao na qual a Snake se esta a mover
	private Direction direction;

	// Lista para a cada pedaco do corpo da nossa Snake, incluindo a sua cabeca
	private static LinkedList<Position> corpo;

	// Define se a Snake esta viva ou nao
	private boolean alive;

	// Define o tamanho da Snake
	private int snakeSize;

	// Criacao de uma nova Snake
	public Snake(Direction starting_direction, int initial_snake_size, int j) {
		corpo = new LinkedList<Position>();

		for(int i = 0; i < initial_snake_size; i++) {
			corpo.add(new Position(i + j + 1, 15));
		}

		snakeSize = initial_snake_size;
		direction = starting_direction;
		alive = true;
	}

	// Retorna o corpo da Snake
	public LinkedList<Position> getCorpo()
	{
		return corpo;
	}

	// Retorna a cabeca da Snake (sera sempre o ultimo elemento da lista)
	public Position getCabeca()
	{
		return corpo.getLast();
	}

	// Retorna o ultimo elemento do corpo da Snake
	public Position getCauda()
	{
		return corpo.getFirst();
	}

	// Define a nova direcao na qual a Snake se ira moviementar
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	// Retorna a direcao na qual a Snake se esta a movimentar
	public Direction getDirection()
	{
		return direction;
	}

	public int getSnakeSize() { return this.snakeSize;}

	// Retorna se a Snake esta viva ou nao
	public boolean isAlive()
	{
		return alive;
	}

	// Mata a Snake
	public void kill()
	{
		alive = false;
	}

	// Move a Snake numa das direcoes definidas em "Direction.java"
	public void move() {
		// Obtem a posicao atual da cabeca
		Position cabeca = getCabeca();

		// Remove a cauda
		corpo.removeFirst();

		// Determina a posicao da cabeca de acordo com a direcao da Snake
		switch(direction) {
			case UP:
				cabeca = new Position(cabeca.getX(), cabeca.getY() - 1);
				break;

			case DOWN:
				cabeca = new Position(cabeca.getX(), cabeca.getY() + 1);
				break;

			case LEFT:
				cabeca = new Position(cabeca.getX() - 1, cabeca.getY());
				break;

			case RIGHT:
				cabeca = new Position(cabeca.getX() + 1, cabeca.getY());
				break;

			default:
				throw new IllegalArgumentException("Não existe essa direção");
		}

		// Insere uma nova cabeca no corpo da Snake
		corpo.addLast(cabeca);
	}

	// Retorna verdade se o corpo da Snake se encontrar numa determinada posicao
	public boolean isCorpo(Position p)
	{
		return corpo.contains(p);
	}


	// Verifica se a cabeca da Snake esta na mesma posicao das cerejas
	public boolean eatCereja(LinkedList<Position> cereja) {
		Position cabeca = getCabeca();

		for(Position p : cereja) {
			if(cabeca.equals(p)) {
				cereja.remove(p);
				return true;
			}
		}

		return false;
	}

	// Verifica se a cabeca da Snake esta na mesma posicao das cerejas Bonus
	public boolean eatCerejaBonus(LinkedList<Position> cerejabonus) {
		Position cabeca = getCabeca();

		for(Position p : cerejabonus) {
			if(cabeca.equals(p)) {
				cerejabonus.remove(p);
				return true;
			}
		}

		return false;
	}

	// Verifica se a posicao da cabeca da snake é a mesma que a do obstaculo
	public boolean colisaoObstaculo(LinkedList<Position> obstaculos) {
		Position cabeca = getCabeca();

		for(Position p : obstaculos) {
			if(cabeca.equals(p)) {
				return true;
			}
		}

		return false;
	}

	// Aumenta o tamanho da Snake
	public void increaseSize() {
		Position cauda = getCauda();

		snakeSize++;
		corpo.addFirst(new Position(cauda.getX(), cauda.getY()));
	}
}