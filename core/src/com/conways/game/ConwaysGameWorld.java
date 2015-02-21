package com.conways.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ConwaysGameWorld implements Screen {
	private Array<Integer> stayAlive;
	private Array<Integer> comeAlive;
	private OrthographicCamera camera;
	private ShapeRenderer renderer;

	private Cell[] board;
	private int width = 100;
	private int height = 100;

	public ConwaysGameWorld(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void show() {
		board = new Cell[width * height];
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
		setDefaultRules();
		randomBoard();
	}

	public void resetBoard() {
		if (board == null) {
			board = new Cell[width * height];
		}
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				board[y * width + x] = new Cell();

			}
	}

	private void randomBoard() {
		resetBoard();
		for (int i = 0; i < board.length; i++) {
			board[i].setAlive(MathUtils.randomBoolean());
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.BLACK);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y * width + x].isAlive()) {
					renderer.setColor(Color.BLACK);
					renderer.rect(x, y, 1, 1);
				} else if (board[y * width + x].isVisited()) {
					renderer.setColor(Color.LIGHT_GRAY);
					renderer.rect(x, y, 1, 1);
				}
			}
		}
		renderer.end();
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
			step();
		if (Gdx.input.isTouched()) {
			Vector3 point = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(point);
			board[MathUtils.floor(point.y) * width + MathUtils.floor(point.x)].setAlive((Gdx.input.isButtonPressed(Buttons.LEFT)));
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}

	public int getNeighbors(int x, int y) {
		int neighbors = 0;
		int locInArr = y * width + x;
		if (locInArr < board.length && locInArr >= 0) {
			// left
			neighbors += getValue(x - 1, y - 1);
			neighbors += getValue(x - 1, y);
			neighbors += getValue(x - 1, y + 1);
			// top and bottom
			neighbors += getValue(x, y - 1);
			neighbors += getValue(x, y + 1);
			// right
			neighbors += getValue(x + 1, y - 1);
			neighbors += getValue(x + 1, y);
			neighbors += getValue(x + 1, y + 1);
		}
		return neighbors;
	}

	public int getValue(int x, int y) {
		int locInArr = y * width + x;
		if (locInArr < board.length && locInArr >= 0) {
			return board[locInArr].isAlive() ? 1 : 0;
		}
		return 0;
	}

	public boolean isAlive(int x, int y) {
		return getValue(x, y) > 0;
	}

	public void step() {
		Cell[] newBoard = new Cell[board.length];
		for (int i = 0; i < newBoard.length; i++) {
			newBoard[i] = new Cell();
			newBoard[i].setVisited(board[i].isVisited());
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int neighbors = getNeighbors(x, y);
				if (isAlive(x, y)) {
					if (stayAlive.contains(neighbors, false))
						newBoard[y * width + x].setAlive(true);
					;
				} else if (comeAlive.contains(neighbors, false)) {
					newBoard[y * width + x].setAlive(true);
					;
				}
			}
		}
		board = newBoard;
	}

	public void setStayAliveRules(int... a) {
		stayAlive = new Array<Integer>();
		for (int i : a)
			stayAlive.add(i);
	}

	public void setComeAliveRules(int... a) {
		comeAlive = new Array<Integer>();
		for (int i : a)
			comeAlive.add(i);
	}

	public void setDefaultRules() {
		setStayAliveRules(2, 3);
		setComeAliveRules(3);
	}
}
