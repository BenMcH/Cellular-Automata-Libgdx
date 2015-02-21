package com.conways.game;

public class Cell {
	private boolean alive;
	private boolean visited;

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
		if (isAlive())
			setVisited(true);
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean bool) {
		this.visited = bool;
	}
}
