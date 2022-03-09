package com.snakegame;

// Representacao da posicao dos varios elementos do jogo no terminal
public class Position {

	// Define a posicao x do elemento
	private int x;

	// Define a posicao y do elemento
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	@Override
	public boolean equals(Object other) {
	    boolean result = false;

	    if(other instanceof Position)
	    {
	        Position o = (Position) other;
	        result = (getX() == o.getX() && getY() == o.getY());
	    }

	    return result;
	}
}
