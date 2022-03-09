package com.snakegame;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

import java.util.LinkedList;

public class GameBuild {
	private final static String EMPTY_STRING = " ";

	private final static String BORDER_STRING = "&";

	// Partes do corpo da Snake
	private final static String SNAKE_HEAD_STRING = "@";
	private final static String SNAKE_BODY_STRING = "O";

	// Cerejas
	private final static String CEREJA_STRING = "$";

	// Cerejas bonus
	private final static String CEREJABONUS_STRING = "?";

	// Obstaculos
	private final static String OBSTACULO_STRING = "#";

	// Valor minimo para as coordenadas x e y
	private final static int X_COORDINATE_OFFSET = 1;
	private final static int Y_COORDINATE_OFFSET = 2;

	// Velocidade da Snake no jogo
	private final static int GAME_LEVEL_1 = 95;
	private final static int GAME_LEVEL_2 = 80;
	private final static int GAME_LEVEL_3 = 65;
	private final static int GAME_LEVEL_4 = 50;
	private final static int GAME_LEVEL_5 = 35;

	// Posicao de cada opcao no menu principal
	private final static int OPTION_LEVEL_1 = 32;
	private final static int OPTION_LEVEL_2 = 34;
	private final static int OPTION_LEVEL_3 = 36;
	private final static int OPTION_LEVEL_4 = 38;
	private final static int OPTION_LEVEL_5 = 40;
	private final static int INSTRUCTIONS_GAME = 14;
	private final static int QUIT_GAME      = 17;

	// Posicao de cada opcao no menu de GAME OVER (START_GAME também no menu principal)
	private final static int START_GAME   = 1;
	private final static int CREATE_LEVEL = 2;
	private final static int RESTART_GAME = 15;
	private final static int MAIN_MENU    = 16;
	private final static int NEXT_LEVEL = 14;
	private final static int MULTIPLAYER = 18;
	private final static int LEADERBOARD = 19;

	// Valores das escolhas do Menu CREATE LEVEL
	private final int CHOOSE_NUMBER_OF_CHERRIES = 9;
	private final int CHOOSE_DIFFICULTY = 10;
	private final int CHOOSE_INITIAL_SNAKE_SIZE = 11;
	private final int CHOOSE_WALLS = 12;
	private final int CHOOSE_MAX_SNAKE_SIZE = 13;
	private final int CHOOSE_TARGET_SCORE = 14;
	private final int CHOOSE_START_GAME = 15;

	// Valores iniciais para Create Level e Multiplayer
	private int NUMBER_OF_CHERRIES = 3;
	private int DIFFICULTY = 1;
	private int INITIAL_SNAKE_SIZE = 1;
	private int WALLS = 1;
	private int MAX_SNAKE_SIZE = 20;
	private int TARGET_SCORE = 20;

	// Verificar se é modo createlevel ou não
	private boolean createlevel = false;

	// Tempo de Jogo
	public long contador = 0;

	// Melhor Tempo cada nivel
	public long LEVEL_1_BESTTIME = Long.MAX_VALUE;
	public long LEVEL_2_BESTTIME = Long.MAX_VALUE;
	public long LEVEL_3_BESTTIME = Long.MAX_VALUE;
	public long LEVEL_4_BESTTIME = Long.MAX_VALUE;
	public long LEVEL_5_BESTTIME = Long.MAX_VALUE;

	// Melhor Jogador cada nivel
	public int LEVEL_1_BESTPLAYER = 0;
	public int LEVEL_2_BESTPLAYER = 0;
	public int LEVEL_3_BESTPLAYER = 0;
	public int LEVEL_4_BESTPLAYER = 0;
	public int LEVEL_5_BESTPLAYER = 0;

	// Atual Jogador cada nivel
	public int LEVEL_1_PLAYER = 0;
	public int LEVEL_2_PLAYER = 0;
	public int LEVEL_3_PLAYER = 0;
	public int LEVEL_4_PLAYER = 0;
	public int LEVEL_5_PLAYER = 0;

	// Contar tempo
	private long time0;
	private long time1;

	// Verificar se é modo multiplayer ou não
	private boolean mult = false;
	int snakewin = 0;

	// Terminal da Lanterna
	private static SwingTerminal terminal;

	// Screen usado no terminal da Lanterna
	private static Screen screen;

	private Game state;

	// Dimensoes do jogo
	public int gameplay_height;
	public int gameplay_width;

	// Nivel selecionado pelo utilizador
	private int nivelSelecionado;

	public GameBuild(int width, int height) {
		terminal = new SwingTerminal(width, height);

		gameplay_width  = width  - X_COORDINATE_OFFSET;
		gameplay_height = height - Y_COORDINATE_OFFSET;

		screen = new Screen(terminal);
		screen.setCursorPosition(null);
		screen.startScreen();
	}

	// Começa um novo jogo
	private void startGame() {
		// Determina a frequencia com que o jogo necessita de ser atualizado
		contador = 0;

		time0 = System.nanoTime();

		if(WALLS == 0) {
			drawWall();
		}
		drawSnake();

		drawString(4, gameplay_height + 1, "SCORE: ", Color.CYAN);
		drawScore(); // score inicial (score será 0)

		if(mult==true){
			drawSnakeMultiplayer();
			drawString(67, gameplay_height + 1, "SCORE 2: ", Color.CYAN);
			drawScoreMultiplayer(); // score inicial (score será 0)
		}


		while(state.isSnakeAlive()) {

			// Gera um novo obstaculo e cereja no início do jogo
			if(contador  == 0) {
				for(int i=0; i < NUMBER_OF_CHERRIES; i++) {
					generateNovaCereja();
					generateNovoObstaculo();
					generateNovaCerejaBonus();
				}
			}

			readKeyboard();

			if(contador % nivelSelecionado == 0)
				updateGame();

			try {
				Thread.sleep(1);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}

			contador++;
		}
	}

	// Vai atualizar o jogo, verifica se apanhou uma cereja, move a Snake, verifica se morreu
	private void updateGame() {

		Position tail = state.getSnakeCauda();
		if(mult==true) {
			Position tailMultiplayer = state.getSnakeMultiplayerCauda();
			clearStringAt(tailMultiplayer.getX(), tailMultiplayer.getY());
		}

		// Apaga a posicao da cauda antes de se mexer para impedir o crescimento infinito da Snake so pelo movimento
		clearStringAt(tail.getX(), tail.getY());

		if(mult && checkHeadToHeadCollision()) {
			state.killSnakeMultiplayer();
			state.killSnake();

			clearScreen();

			openDrawMultiplayerMenu();
		}

		// Move a Snake e volta a desenha-la
		state.moveSnake();

		if(mult && checkHeadToHeadCollision()) {
			state.killSnakeMultiplayer();
			state.killSnake();

			clearScreen();

			openDrawMultiplayerMenu();
		}

		if(mult==true){
			state.moveSnakeMultiplayer();
			drawSnakeMultiplayer();
		}

		drawSnake();

		if(mult==false) {
			// Guarda a posicao da cabeca da Snake antes de se mover
			Position head = state.getSnakeCabeca();

			if (state.getScore() >= TARGET_SCORE) {
				state.killSnake();

				time1 = System.nanoTime();

				contador = (time1-time0)/1000000;

				clearScreen();

				if (nivelSelecionado == GAME_LEVEL_5 || createlevel) {
					openLastLevelMenu();
				} else {
					openGameWinMenu();
				}
			} else if (state.snakeEatCereja()) {
				drawScore();

				generateNovaCereja();
				generateNovoObstaculo();
			} else if (state.snakeEatCerejaBonus()) {
				drawScore();
				drawScore();

				generateNovaCerejaBonus();
				generateNovoObstaculo();
				generateNovoObstaculo();
			} else if (checkCollision()) {
				mostraCrashPosition(head.getX(), head.getY());

				// Matar a Snake para o jogo acabar
				state.killSnake();

				clearScreen();

				openGameOverMenu();
			}
		}else{
			// Guarda a posicao da cabeca da Snake antes de se mover
			Position head = state.getSnakeCabeca();
			Position headMultiplayer = state.getSnakeMultiplayerCabeca();


			if (checkCollision()) {
				mostraCrashPosition(head.getX(), head.getY());

				// Matar a Snake para o jogo acabar
				snakewin = 2;
				state.killSnake();

				openWinMultiplayerMenu();
			}else if(checkCollisionMultiplayer()){
				mostraCrashPosition(headMultiplayer.getX(), headMultiplayer.getY());

				snakewin = 1;
				state.killSnakeMultiplayer();

				openWinMultiplayerMenu();

			} else if (state.snakeEatCereja()) {
				drawScore();

				generateNovaCereja();
				generateNovoObstaculo();


			}else if (state.snakeMultiplayerEatCereja()) {
				drawScoreMultiplayer();

				generateNovaCereja();
				generateNovoObstaculo();


			} else if (state.snakeEatCerejaBonus()) {
				drawScore();
				drawScore();

				generateNovaCerejaBonus();
				generateNovoObstaculo();
				generateNovoObstaculo();
			} else if (state.snakeMultiplayerEatCerejaBonus()) {
				drawScoreMultiplayer();
				drawScoreMultiplayer();

				generateNovaCerejaBonus();
				generateNovoObstaculo();
				generateNovoObstaculo();
			}else if (state.getScore() >= TARGET_SCORE) {
				snakewin = 1;
				state.killSnake();
				openWinMultiplayerMenu();
			}else if (state.getScoreMultiplayer() >= TARGET_SCORE) {
				snakewin = 2;
				state.killSnakeMultiplayer();
				openWinMultiplayerMenu();
			}
		}


		refreshScreen();
	}

	// Verifica qual a proxima direcao que a Snake ira tomar
	private void readKeyboard() {
		Key k = readKeyInput();
		if(k != null) {
			switch (k.getKind()) {
				case ArrowUp:
					state.setDirection(Direction.UP);
					break;

				case ArrowDown:
					state.setDirection(Direction.DOWN);
					break;

				case ArrowLeft:
					state.setDirection(Direction.LEFT);
					break;

				case ArrowRight:
					state.setDirection(Direction.RIGHT);
					break;

				default:
					break;
			}
			if (mult) {
				switch (k.getCharacter()) {
					case 'w':
						state.setDirectionMultiplayer(Direction.UP);
						break;

					case 's':
						state.setDirectionMultiplayer(Direction.DOWN);
						break;

					case 'a':
						state.setDirectionMultiplayer(Direction.LEFT);
						break;

					case 'd':
						state.setDirectionMultiplayer(Direction.RIGHT);
						break;
					default:
						break;
				}
			}
		}
	}

	// Desenha as Paredes do jogo
	private void drawWall() {
		// Desenha na parte de cima e de baixo do jogo
		for(int i = 0; i < gameplay_width; i++) {
			drawString(i, 0, BORDER_STRING, null);
			drawString(i, gameplay_height, BORDER_STRING, null);
		}

		// Desenha na parte esquerda e direita do jogo
		for(int i = 0; i <= gameplay_height; i++) {
			drawString(0, i, BORDER_STRING, null);
			drawString(gameplay_width, i, BORDER_STRING, null);
		}
	}
	// Verifica se a posicao da parede é igual a posicao dada
	private boolean isWall(Position p) {
		if(p.getX() == 0 || p.getX() == gameplay_width || p.getY() == 0 || p.getY() == gameplay_height) {
			return true;
		}

		return false;
	}

	// Verifica se a Snake embateu num obstaculo, numa parede ou nela propria
	private boolean checkCollision() {
		Position head = state.getSnakeCabeca();

		// Colisao com obstaculos
		if(state.snakeColisaoObstaculo())
			return true;

		//Colisao com as paredes
		if(WALLS == 0) {
			if(isWall(head))
				return true;
		}

		LinkedList<Position> corpo = state.getSnakeCorpo();

		// Colisao com ela mesma
		if(corpo.subList(0, corpo.size()-1).contains(head))
			return true;

		if(mult){
			// Colisao com a outra Snake
			LinkedList<Position> corpoMultiplayer = state.getSnakeMultiplayerCorpo();
			if(corpoMultiplayer.subList(0, corpoMultiplayer.size()-1).contains(head))
				return true;
		}
		return false;
	}
	private boolean checkCollisionMultiplayer() {
		Position head = state.getSnakeMultiplayerCabeca();

		// Colisao com obstaculos
		if(state.snakeMultiplayerColisaoObstaculo())
			return true;

		//Colisao com as paredes
		if(WALLS == 0) {
			if(isWall(head))
				return true;
		}

		LinkedList<Position> corpo = state.getSnakeMultiplayerCorpo();

		// Colisao com ela mesma
		if(corpo.subList(0, corpo.size()-1).contains(head))
			return true;

		// Colisao com a outra Snake
		LinkedList<Position> corpoSingleplayer = state.getSnakeCorpo();
		if(corpoSingleplayer.subList(0, corpoSingleplayer.size()-1).contains(head))
			return true;

		return false;
	}
	private boolean checkHeadToHeadCollision() {
		Position head1 = state.getSnakeCabeca();
		Position head2 = state.getSnakeMultiplayerCabeca();
		if (head1.equals(head2)) {
			return true;
		}
		return false;
	}

	// Desenho das Strings do screen com uma dada posicao e cor
	private void drawString(int x, int y, String string, Terminal.Color fg_color) {
		screen.putString(x, y, string, fg_color, null);
	}
	// Apaga as Strings numa dada posicao
	private void clearStringAt(int x, int y) {
		drawString(x, y, EMPTY_STRING, null);
	}

	// Cria o menu principal
	public void openMainMenu() {
		nivelSelecionado = GAME_LEVEL_1;
		NUMBER_OF_CHERRIES = 3;
		INITIAL_SNAKE_SIZE = 1;
		MAX_SNAKE_SIZE = 20;
		TARGET_SCORE = 20;
		WALLS = 1;
		mult = false;
		createlevel = false;
		snakewin = 0;

		drawMainMenu();

		refreshScreen();

		int selected_option = opcaoMainMenu();

		if(selected_option == START_GAME) {
			clearScreen();

			state = new Game(MAX_SNAKE_SIZE, INITIAL_SNAKE_SIZE, mult);

			if(nivelSelecionado==GAME_LEVEL_1){
				LEVEL_1_PLAYER += 1;
				TARGET_SCORE = 10;
			}else if(nivelSelecionado==GAME_LEVEL_2){
				LEVEL_2_PLAYER += 1;
				TARGET_SCORE = 15;
			}else if(nivelSelecionado==GAME_LEVEL_3){
				LEVEL_3_PLAYER += 1;
				TARGET_SCORE = 20;
			}else if(nivelSelecionado==GAME_LEVEL_4){
				LEVEL_4_PLAYER += 1;
				TARGET_SCORE = 25;
			}else if(nivelSelecionado==GAME_LEVEL_5){
				LEVEL_5_PLAYER += 1;
				TARGET_SCORE = 30;
			}

			startGame();

		}else if(selected_option == MULTIPLAYER){
			clearScreen();
			openMultiplayerMenu();
		}
		else if (selected_option == CREATE_LEVEL) {
			clearScreen();

			openCreateLevelMenu();
		}
		else if(selected_option == LEADERBOARD){
			clearScreen();
			openLeaderboardMenu();

			refreshScreen();
		}
		else if (selected_option== INSTRUCTIONS_GAME){
			clearScreen();
			openInstructionsMenu();

			refreshScreen();

		} else {
			exitGame();
		}
	}
	// Verifica a opcao selecionada no menu principal
	private int opcaoMainMenu() {
		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getCharacter()) {
					case '1':
						nivelSelecionado = GAME_LEVEL_1;
						WALLS = 1;
						representacaoGraficaMainMenu(OPTION_LEVEL_1);
						break;

					case '2':
						nivelSelecionado = GAME_LEVEL_2;
						WALLS = 1;
						representacaoGraficaMainMenu(OPTION_LEVEL_2);
						break;

					case '3':
						nivelSelecionado = GAME_LEVEL_3;
						WALLS = 1;
						representacaoGraficaMainMenu(OPTION_LEVEL_3);
						break;

					case '4':
						nivelSelecionado = GAME_LEVEL_4;
						WALLS = 0;
						representacaoGraficaMainMenu(OPTION_LEVEL_4);
						break;

					case '5':
						nivelSelecionado = GAME_LEVEL_5;
						WALLS = 0;
						representacaoGraficaMainMenu(OPTION_LEVEL_5);
						break;

					case 's':
					case 'S':
						return START_GAME;
					case 'm':
					case 'M':
						return MULTIPLAYER;
					case 'c':
					case 'C':
						return CREATE_LEVEL;
					case 'l':
					case 'L':
						return LEADERBOARD;
					case 'i':
					case 'I':
						return INSTRUCTIONS_GAME;
					case 'q':
					case 'Q':
						return QUIT_GAME;

					default:
						break;
				}
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu principal
	private void representacaoGraficaMainMenu(int selected) {
		int y = 20;

		drawString(OPTION_LEVEL_1, y, "1", Color.BLUE);
		drawString(OPTION_LEVEL_2, y, "2", Color.BLUE);
		drawString(OPTION_LEVEL_3, y, "3", Color.BLUE);
		drawString(OPTION_LEVEL_4, y, "4", Color.BLUE);
		drawString(OPTION_LEVEL_5, y, "5", Color.BLUE);

		if(selected == OPTION_LEVEL_1) {
			drawString(OPTION_LEVEL_1, y, "1", Color.WHITE);
		} else if(selected == OPTION_LEVEL_2) {
			drawString(OPTION_LEVEL_2, y, "2", Color.WHITE);
		} else if(selected == OPTION_LEVEL_3) {
			drawString(OPTION_LEVEL_3, y, "3", Color.WHITE);
		} else if(selected == OPTION_LEVEL_4) {
			drawString(OPTION_LEVEL_4, y, "4", Color.WHITE);
		} else if(selected == OPTION_LEVEL_5) {
			drawString(OPTION_LEVEL_5, y, "5", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu principal
	private void drawMainMenu() {
		int x = 13;
		int y = 1;

		drawString(x, y, "########  ###     ##  #########  ###    ###  ########  ", Color.CYAN);
		drawString(x,++y,"##        ## ##   ##  ###   ###  ###   ###   ##       ", Color.CYAN);
		drawString(x,++y,"########  ##  ##  ##  #########  #######     #####     ", Color.CYAN);
		drawString(x,++y,"      ##  ##   ## ##  ###   ###  ###   ###   ##       ", Color.CYAN);
		drawString(x,++y,"########  ##    ####  ###   ###  ###    ###  ########  ", Color.CYAN);
		drawString(x+5,++y,"########  #########  ###      ###  ########       ", Color.CYAN);
		drawString(x+5,++y,"##        ###   ###  ####    ####  ##            ", Color.CYAN);
		drawString(x+5,++y,"##  ####  #########  ## ##  ## ##  #####          ", Color.CYAN);
		drawString(x+5,++y,"##    ##  ###   ###  ##   ##   ##  ##            ", Color.CYAN);
		drawString(x+5,++y,"########  ###   ###  ##        ##  ########       ", Color.CYAN);

		y += 2;
		x = 25;

		drawString(x, y,   "##################################", Color.BLUE);
		drawString(x, ++y, "Press 'S' to start the game", Color.BLUE);
		drawString(x, ++y, "Press 'M' to play multiplayer mode", Color.BLUE);
		drawString(x, ++y, "Press 'C' to create your own level", Color.BLUE);
		drawString(x, ++y, "Press 'L' to go to leaderboard", Color.BLUE);
		drawString(x, ++y, "Press 'I' to see game instructions", Color.BLUE);
		drawString(x, ++y, "Press 'Q' to quit the game", Color.BLUE);

		y++;

		drawString(x, ++y,  "Level:", Color.BLUE);
		drawString(OPTION_LEVEL_1, y,  "1", Color.WHITE);
		drawString(OPTION_LEVEL_2, y,  "2", Color.BLUE);
		drawString(OPTION_LEVEL_3, y, "3", Color.BLUE);
		drawString(OPTION_LEVEL_4, y, "4", Color.BLUE);
		drawString(OPTION_LEVEL_5, y, "5", Color.BLUE);
		drawString(x, ++y, "##################################", Color.BLUE);
	}

	// Cria o menu GAME OVER
	private void openGameOverMenu() {
		// Apaga todos os obstaculos e cerejas
		clearGameObjects();

		drawGameOverMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoGameOverMenu();

		if(opcaoSelecionada == RESTART_GAME) {
			clearScreen();

			state = new Game(MAX_SNAKE_SIZE, INITIAL_SNAKE_SIZE, mult);

			if(nivelSelecionado==GAME_LEVEL_1){
				LEVEL_1_PLAYER += 1;
			}else if(nivelSelecionado==GAME_LEVEL_2){
				LEVEL_2_PLAYER += 1;
			}else if(nivelSelecionado==GAME_LEVEL_3){
				LEVEL_3_PLAYER += 1;
			}else if(nivelSelecionado==GAME_LEVEL_4){
				LEVEL_4_PLAYER += 1;
			}else if(nivelSelecionado==GAME_LEVEL_5){
				LEVEL_5_PLAYER += 1;
			}

			startGame();
		} else if(opcaoSelecionada == MAIN_MENU) {
			clearScreen();

			openMainMenu();
		} else if(opcaoSelecionada == QUIT_GAME) {
			exitGame();
		}
	}
	// Retornando a opcao selecionada no menu GAME OVER
	private int opcaoGameOverMenu() {
		int selected  = RESTART_GAME;

		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getKind()) {
					case ArrowDown:
						if(selected < QUIT_GAME)
							selected++;

						break;

					case ArrowUp:
						if(selected > RESTART_GAME)
							selected--;

						break;

					case Enter:
						return selected;

					default:
						break;
				}

				representacaoGraficaGameOverMenu(selected);
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu GAME OVER
	private void representacaoGraficaGameOverMenu(int selected) {
		drawString(28, RESTART_GAME, "Restart", Color.BLUE);
		drawString(28, MAIN_MENU, "Back to main menu", Color.BLUE);
		drawString(28, QUIT_GAME, "Quit Game", Color.BLUE);

		if(selected == RESTART_GAME)
		{
			drawString(28, RESTART_GAME, "Restart", Color.WHITE);
		}
		else if(selected == MAIN_MENU)
		{
			drawString(28, MAIN_MENU, "Back to main menu", Color.WHITE);
		}
		else if(selected == QUIT_GAME)
		{
			drawString(28, QUIT_GAME, "Quit Game", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu GAME OVER
	private void drawGameOverMenu() {
		int x = 20;
		int y = 2;

		drawString(x, y,  "#######  #######  ##### #####  ######", Color.CYAN);
		drawString(x, ++y,"##       ##   ##  ## ## ## ##  ##    ", Color.CYAN);
		drawString(x, ++y,"## ####  #######  ## ##### ##  ######", Color.CYAN);
		drawString(x, ++y,"##   ##  ##   ##  ##  ###  ##  ##    ", Color.CYAN);
		drawString(x, ++y,"#######  ##   ##  ##       ##  ######", Color.CYAN);

		y++;

		drawString(x, ++y,"########  ###  ###  ######  ######### ", Color.CYAN);
		drawString(x, ++y,"##    ##  ###  ###  ###     ###  ###  ", Color.CYAN);
		drawString(x, ++y,"##    ##   ######   ######  ########  ", Color.CYAN);
		drawString(x, ++y,"##    ##    ####    ###     ###   ### ", Color.CYAN);
		drawString(x, ++y,"########     ##     ######  ###     ##", Color.CYAN);

		y++;
		x = 28;

		drawString(x, ++y, "####################", Color.BLUE);
		drawString(x, ++y, "Restart", Color.WHITE);
		drawString(x, ++y, "Back to main menu", Color.BLUE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "####################", Color.BLUE);
	}

	// Cria o menu GAME WIN
	private void openGameWinMenu() {
		// Apaga todos os obstaculos e cerejas
		clearGameObjects();

		if(nivelSelecionado==GAME_LEVEL_1 && contador<LEVEL_1_BESTTIME){
			LEVEL_1_BESTPLAYER = LEVEL_1_PLAYER;
			LEVEL_1_BESTTIME = contador;
		}else if(nivelSelecionado==GAME_LEVEL_2 && contador<LEVEL_2_BESTTIME){
			LEVEL_2_BESTPLAYER = LEVEL_2_PLAYER;
			LEVEL_2_BESTTIME = contador;
		}else if(nivelSelecionado==GAME_LEVEL_3 && contador<LEVEL_3_BESTTIME){
			LEVEL_3_BESTPLAYER = LEVEL_3_PLAYER;
			LEVEL_3_BESTTIME = contador;
		}else if(nivelSelecionado==GAME_LEVEL_4 && contador<LEVEL_4_BESTTIME){
			LEVEL_4_BESTPLAYER = LEVEL_4_PLAYER;
			LEVEL_4_BESTTIME = contador;
		}

		drawGameWinMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoGameWinMenu();

		if(opcaoSelecionada == NEXT_LEVEL) {
			clearScreen();
			nivelSelecionado -= 15;

			state = new Game(MAX_SNAKE_SIZE, INITIAL_SNAKE_SIZE, mult);

			if(nivelSelecionado == GAME_LEVEL_4 || nivelSelecionado == GAME_LEVEL_5) WALLS = 0;

			if(nivelSelecionado==GAME_LEVEL_2){
				LEVEL_2_PLAYER += 1;
				TARGET_SCORE = 15;
			}else if(nivelSelecionado==GAME_LEVEL_3){
				LEVEL_3_PLAYER += 1;
				TARGET_SCORE = 20;
			}else if(nivelSelecionado==GAME_LEVEL_4){
				LEVEL_4_PLAYER += 1;
				TARGET_SCORE = 25;
			}else if(nivelSelecionado==GAME_LEVEL_5){
				LEVEL_5_PLAYER += 1;
				TARGET_SCORE = 30;
			}

			startGame();
		} else if(opcaoSelecionada == RESTART_GAME) {
			clearScreen();

			state = new Game(MAX_SNAKE_SIZE, INITIAL_SNAKE_SIZE, mult);

			if(nivelSelecionado==GAME_LEVEL_1){
				LEVEL_1_PLAYER += 1;
			}else if(nivelSelecionado==GAME_LEVEL_2){
				LEVEL_2_PLAYER += 1;
			}else if(nivelSelecionado==GAME_LEVEL_3){
				LEVEL_3_PLAYER += 1;
			}else if(nivelSelecionado==GAME_LEVEL_4){
				LEVEL_4_PLAYER += 1;
			}

			startGame();
		} else if(opcaoSelecionada == MAIN_MENU) {
			clearScreen();

			openMainMenu();
		} else if(opcaoSelecionada == QUIT_GAME) {
			exitGame();
		}
	}
	// Cria o menu Last Level
	private void openLastLevelMenu() {
		// Apaga todos os obstaculos e cerejas
		clearGameObjects();

		if(nivelSelecionado==GAME_LEVEL_5 && contador<LEVEL_5_BESTTIME){
			LEVEL_5_BESTPLAYER = LEVEL_5_PLAYER;
			LEVEL_5_BESTTIME = contador;
		}

		drawLastLevelMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoGameOverMenu();

		if(opcaoSelecionada == RESTART_GAME) {
			clearScreen();

			state = new Game(MAX_SNAKE_SIZE, INITIAL_SNAKE_SIZE, mult);

			if(nivelSelecionado==GAME_LEVEL_5){
				LEVEL_5_PLAYER += 1;
			}

			startGame();
		} else if(opcaoSelecionada == MAIN_MENU) {
			clearScreen();

			openMainMenu();
		} else if(opcaoSelecionada == QUIT_GAME) {
			exitGame();
		}
	}
	// Retornando a opcao selecionada no menu GAME WIN
	private int opcaoGameWinMenu() {
		int selected  = NEXT_LEVEL;

		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getKind()) {
					case ArrowDown:
						if(selected < QUIT_GAME)
							selected++;


						break;

					case ArrowUp:
						if(selected > NEXT_LEVEL)
							selected--;

						break;

					case Enter:
						return selected;

					default:
						break;
				}

				representacaoGraficaGameWinMenu(selected);
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu GAME WIN
	private void representacaoGraficaGameWinMenu(int selected) {
		drawString(28, NEXT_LEVEL+1, "Next Level", Color.BLUE);
		drawString(28, RESTART_GAME+1, "Restart Level", Color.BLUE);
		drawString(28, MAIN_MENU+1, "Back to main menu", Color.BLUE);
		drawString(28, QUIT_GAME+1, "Quit Game", Color.BLUE);

		if(selected == NEXT_LEVEL) {
			drawString(28, NEXT_LEVEL+1, "Next Level", Color.WHITE);
		}
		else if(selected == RESTART_GAME)
		{
			drawString(28, RESTART_GAME+1, "Restart Level", Color.WHITE);
		}
		else if(selected == MAIN_MENU)
		{
			drawString(28, MAIN_MENU+1, "Back to main menu", Color.WHITE);
		}
		else if(selected == QUIT_GAME)
		{
			drawString(28, QUIT_GAME+1, "Quit Game", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu GAME WIN
	private void drawGameWinMenu() {
		int x = 26;
		int y = 2;

		drawString(x, y,  "#     #  #######  ##    ##", Color.CYAN);
		drawString(x, ++y," #   #   ##   ##  ##    ##", Color.CYAN);
		drawString(x, ++y,"  ###    ##   ##  ##    ##", Color.CYAN);
		drawString(x, ++y,"   #     ##   ##  ##    ##", Color.CYAN);
		drawString(x, ++y,"   #     #######  ########", Color.CYAN);

		y++;

		drawString(x, ++y,"##        ##  ###  ###     ##", Color.CYAN);
		drawString(x, ++y,"##   ##   ##  ###  ## ##   ##", Color.CYAN);
		drawString(x, ++y,"## ##  ## ##  ###  ##  ##  ##", Color.CYAN);
		drawString(x, ++y,"####    ####  ###  ##   ## ##", Color.CYAN);
		drawString(x, ++y,"###      ###  ###  ##    ####", Color.CYAN);

		y++;
		x = 28;

		drawString(x, ++y, "#######################", Color.BLUE);
		drawString(x, ++y, "Next Level", Color.WHITE);
		drawString(x, ++y, "Restart Level", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.BLUE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "#######################", Color.BLUE);
		if(!createlevel) {
			drawString(x, ++y, "Score saved as", Color.BLUE);
			drawString(x+15, y, "Player ", Color.CYAN);
		}
		if(nivelSelecionado==GAME_LEVEL_1){
			drawString(x+22, y, Integer.toString(LEVEL_1_PLAYER), Color.CYAN);
		}else if(nivelSelecionado==GAME_LEVEL_2){
			drawString(x+22, y, Integer.toString(LEVEL_2_PLAYER), Color.CYAN);
		}else if(nivelSelecionado==GAME_LEVEL_3){
			drawString(x+22, y, Integer.toString(LEVEL_3_PLAYER), Color.CYAN);
		}else if(nivelSelecionado==GAME_LEVEL_4){
			drawString(x+22, y, Integer.toString(LEVEL_4_PLAYER), Color.CYAN);
		}
	}
	// Desenha o menu LAST LEVEL
	private void drawLastLevelMenu() {
		int x = 26;
		int y = 2;

		drawString(x, y,  "#     #  #######  ##    ##", Color.CYAN);
		drawString(x, ++y," #   #   ##   ##  ##    ##", Color.CYAN);
		drawString(x, ++y,"  ###    ##   ##  ##    ##", Color.CYAN);
		drawString(x, ++y,"   #     ##   ##  ##    ##", Color.CYAN);
		drawString(x, ++y,"   #     #######  ########", Color.CYAN);

		y++;

		drawString(x, ++y,"##        ##  ###  ###     ##", Color.CYAN);
		drawString(x, ++y,"##   ##   ##  ###  ## ##   ##", Color.CYAN);
		drawString(x, ++y,"## ##  ## ##  ###  ##  ##  ##", Color.CYAN);
		drawString(x, ++y,"####    ####  ###  ##   ## ##", Color.CYAN);
		drawString(x, ++y,"###      ###  ###  ##    ####", Color.CYAN);

		y++;
		x = 28;

		drawString(x, ++y, "#######################", Color.BLUE);
		drawString(x, ++y, "Restart", Color.WHITE);
		drawString(x, ++y, "Back to main menu", Color.BLUE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "#######################", Color.BLUE);
		if(!createlevel) {
			drawString(x, ++y, "Score saved as", Color.BLUE);
			drawString(x+15, y, "Player ", Color.CYAN);
		}
		if(nivelSelecionado==GAME_LEVEL_5) {
			drawString(x + 22, y, Integer.toString(LEVEL_5_PLAYER), Color.CYAN);
		}
	}

	// Cria o menu Instructions
	private void openInstructionsMenu() {

		drawInstructionsMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoInstructionsMenu();

		if(opcaoSelecionada == MAIN_MENU)
		{
			clearScreen();

			openMainMenu();
		}
		else if(opcaoSelecionada == QUIT_GAME)
		{
			exitGame();
		}
	}
	// Retornando a opcao selecionada no menu Instructions
	private int opcaoInstructionsMenu() {
		int selected  = MAIN_MENU;

		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getKind()) {
					case ArrowDown:
						if(selected < QUIT_GAME)
							selected++;

						break;

					case ArrowUp:
						if(selected > MAIN_MENU)
							selected--;

						break;

					case Enter:
						return selected;

					default:
						break;
				}

				representacaoGraficaInstructionMenu(selected);
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu Instructions
	private void representacaoGraficaInstructionMenu(int selected) {

		drawString(5, MAIN_MENU+3, "Back to main menu", Color.BLUE);
		drawString(5, QUIT_GAME+3, "Quit Game", Color.BLUE);


		if(selected == MAIN_MENU) {
			drawString(5, MAIN_MENU+3, "Back to main menu", Color.WHITE);
		} else if(selected == QUIT_GAME) {
			drawString(5, QUIT_GAME+3, "Quit Game", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu INSTRUCTIONS
	private void drawInstructionsMenu() {
		int x = 5;
		int y = 1;

		drawString(x, y,   "######################################################################", Color.BLUE);
		drawString(x, ++y, "To Move the Snake:", Color.BLUE);
		drawString(x, ++y, "- Press ArrowUp to go up (and 'w' in Multiplayer Mode)", Color.BLUE);
		drawString(x, ++y, "- Press ArrowDown to go down (and 's' in Multiplayer Mode)", Color.BLUE);
		drawString(x, ++y, "- Press ArrowRight to go right (and 'd' in Multiplayer Mode)", Color.BLUE);
		drawString(x, ++y, "- Press ArrowLeft to go left (and 'a' in Multiplayer Mode)", Color.BLUE);
		drawString(x, ++y,   "######################################################################", Color.BLUE);
		drawString(x, ++y, "The Game Rules:", Color.BLUE);
		drawString(x, ++y, "- You need to grab the cherries to complete the levels", Color.BLUE);
		drawString(x, ++y, "- Every normal cherry ('$') increase the score in 1", Color.BLUE);
		drawString(x, ++y, "- Every bonus cherry ('?') increase the score in 2", Color.BLUE);
		drawString(x, ++y, "- You die if you collide with yourself (O), obstacles (#) or walls (&)", Color.BLUE);
		drawString(x, ++y, "######################################################################", Color.BLUE);
		drawString(x,++y, "Multiplayer Mode: ", Color.BLUE);
		drawString(x,++y, "- First Snake to reach target score wins.", Color.BLUE);
		drawString(x,++y, "- If you collide into your opponent's body or an obstacle you lose", Color.BLUE);
		drawString(x,++y, "- If both snakes collide head to head, it's a draw", Color.BLUE);

		y++;

		drawString(x, y, "######################################################################", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.WHITE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "######################################################################", Color.BLUE);
	}

	// Cria o menu CREATE LEVEL
	private void openCreateLevelMenu() {
		NUMBER_OF_CHERRIES = 5;
		DIFFICULTY = 3;
		INITIAL_SNAKE_SIZE = 3;
		WALLS = 0;
		MAX_SNAKE_SIZE = 20;
		TARGET_SCORE = 20;
		createlevel = true;

		drawCreateLevelMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoCreateLevelMenu();

		if(opcaoSelecionada == CHOOSE_START_GAME) {
			clearScreen();

			switch (DIFFICULTY) {
				case 1:
					nivelSelecionado = GAME_LEVEL_1;
					break;
				case 2:
					nivelSelecionado = GAME_LEVEL_2;
					break;
				case 3:
					nivelSelecionado = GAME_LEVEL_3;
					break;
				case 4:
					nivelSelecionado = GAME_LEVEL_4;
					break;
				case 5:
					nivelSelecionado = GAME_LEVEL_5;
					break;
				default:
					break;
			}

			state = new Game(MAX_SNAKE_SIZE, INITIAL_SNAKE_SIZE, mult);
			startGame();

		}
		else if(opcaoSelecionada == MAIN_MENU)
		{
			clearScreen();

			openMainMenu();
		}
		else if(opcaoSelecionada == QUIT_GAME)
		{
			exitGame();
		}
	}
	// Retornando a opcao selecionada no menu CREATE LEVEL
	private int opcaoCreateLevelMenu() {
		int selected  = CHOOSE_NUMBER_OF_CHERRIES;

		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getKind()) {
					case ArrowDown:
						if(selected < QUIT_GAME)
							selected++;

						break;

					case ArrowUp:
						if(selected > CHOOSE_NUMBER_OF_CHERRIES)
							selected--;

						break;
					case ArrowRight:
						if(selected == CHOOSE_NUMBER_OF_CHERRIES && NUMBER_OF_CHERRIES < 50) {
							NUMBER_OF_CHERRIES++;
						}
						else if(selected == CHOOSE_DIFFICULTY && DIFFICULTY < 5) {
							DIFFICULTY++;
						}
						else if(selected == CHOOSE_INITIAL_SNAKE_SIZE && INITIAL_SNAKE_SIZE < 10) {
							if(INITIAL_SNAKE_SIZE == MAX_SNAKE_SIZE) {
								MAX_SNAKE_SIZE++;
							}
							INITIAL_SNAKE_SIZE++;
						}
						else if(selected == CHOOSE_WALLS && WALLS < 1) {
							WALLS++;
						}
						else if(selected == CHOOSE_MAX_SNAKE_SIZE && MAX_SNAKE_SIZE < 100) {
							MAX_SNAKE_SIZE++;
						}
						else if (selected == CHOOSE_TARGET_SCORE && TARGET_SCORE < 100) {
							TARGET_SCORE++;
						}
						break;
					case ArrowLeft:
						if(selected == CHOOSE_NUMBER_OF_CHERRIES && NUMBER_OF_CHERRIES > 1) {
							NUMBER_OF_CHERRIES--;
						}
						else if(selected == CHOOSE_DIFFICULTY && DIFFICULTY > 1) {
							DIFFICULTY--;
						}
						else if(selected == CHOOSE_INITIAL_SNAKE_SIZE && INITIAL_SNAKE_SIZE > 1) {
							INITIAL_SNAKE_SIZE--;
						}
						else if(selected == CHOOSE_WALLS && WALLS > 0) {
							WALLS--;
						}
						else if(selected == CHOOSE_MAX_SNAKE_SIZE && MAX_SNAKE_SIZE > INITIAL_SNAKE_SIZE) {
							MAX_SNAKE_SIZE--;
						}
						else if (selected == CHOOSE_TARGET_SCORE && TARGET_SCORE > 1) {
							TARGET_SCORE--;
						}
						break;
					case Enter:
						if(selected == QUIT_GAME || selected == CHOOSE_START_GAME || selected == MAIN_MENU) {
							return selected;
						}
						else break;
					default:
						break;
				}

				representacaoGraficaCreateLevelMenu(selected);
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu CREATE LEVEL
	private void representacaoGraficaCreateLevelMenu(int selected) {

		drawString(5, 3, "CREATE YOUR LEVEL:", Color.BLUE);
		drawString(5, 4, "- Number of Cherries: " + NUMBER_OF_CHERRIES, Color.BLUE);
		drawString(5, 5, "- Difficulty: " + DIFFICULTY, Color.BLUE);
		drawString(5, 6, "- Initial Snake size: " + INITIAL_SNAKE_SIZE, Color.BLUE);
		drawString(5, 7, "- Walls: Yes No", Color.BLUE);
		drawString(5, 8, "- Max Snake Size: " + MAX_SNAKE_SIZE, Color.BLUE);
		drawString(5, 9, "- Target Score: " + TARGET_SCORE, Color.BLUE);
		drawString(5, 18, "Start Game", Color.BLUE);
		drawString(5, MAIN_MENU+3, "Back to main menu", Color.BLUE);
		drawString(5, QUIT_GAME+3, "Quit Game", Color.BLUE);

		if(selected == CHOOSE_NUMBER_OF_CHERRIES) {
			drawString(5, 4, "- Number of Cherries: " + NUMBER_OF_CHERRIES + "  ", Color.WHITE);
		}
		else if(selected == CHOOSE_DIFFICULTY) {
			drawString(5, 5, "- Difficulty: " + DIFFICULTY + "  ", Color.WHITE);
		}
		else if(selected == CHOOSE_INITIAL_SNAKE_SIZE) {
			drawString(5, 6, "- Initial Snake size: " + INITIAL_SNAKE_SIZE + "  ", Color.WHITE);
		}
		else if(selected == CHOOSE_WALLS) {
			if(WALLS == 0) {
				drawString(5, 7, "- Walls: Yes", Color.WHITE);
				drawString(18, 7, "No", Color.BLUE);
			}
			else if(WALLS == 1) {
				drawString(5, 7, "- Walls: ", Color.WHITE);
				drawString(14, 7, "Yes" , Color.BLUE);
				drawString(18, 7, "No", Color.WHITE);
			}
		}
		else if(selected == CHOOSE_MAX_SNAKE_SIZE) {
			drawString(5, 8, "- Max Snake Size: " + MAX_SNAKE_SIZE + "  ", Color.WHITE);
		}
		else if (selected == CHOOSE_TARGET_SCORE) {
			drawString(5, 9, "- Target Score: " + TARGET_SCORE + "  ", Color.WHITE);
		}
		else if (selected == CHOOSE_START_GAME) {
			drawString(5, 18, "Start Game", Color.WHITE);
		}
		else if(selected == MAIN_MENU) {
			drawString(5, 19, "Back to main menu", Color.WHITE);
		} else if(selected == QUIT_GAME) {
			drawString(5, 20, "Quit Game", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu CREATE LEVEL
	private void drawCreateLevelMenu() {
		int x = 5;
		int y = 2;

		drawString(x, y,   "######################################################################", Color.BLUE);
		drawString(x, ++y, "CREATE YOUR LEVEL:", Color.BLUE);
		drawString(x, ++y, "- Number of Cherries: " + NUMBER_OF_CHERRIES, Color.WHITE);
		drawString(x, ++y, "- Difficulty: " + DIFFICULTY, Color.BLUE);
		drawString(x, ++y, "- Initial Snake size: " + INITIAL_SNAKE_SIZE, Color.BLUE);
		drawString(x, ++y, "- Walls: Yes No", Color.BLUE);
		drawString(x, ++y, "- Max Snake Size: " + MAX_SNAKE_SIZE, Color.BLUE);
		drawString(x, ++y, "- Target Score: " + TARGET_SCORE, Color.BLUE);
		drawString(x, ++y, "######################################################################", Color.BLUE);

		y += 7;

		drawString(x, y, "######################################################################", Color.BLUE);
		drawString(x, ++y,"Start Game", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.BLUE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "######################################################################", Color.BLUE);
	}

	// Cria o menu MULTIPLAYER
	private void openMultiplayerMenu() {
		mult = true;
		DIFFICULTY = 3;
		WALLS = 0;
		TARGET_SCORE = 20;

		drawMultiplayerMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoMultiplayerMenu();

		if(opcaoSelecionada == CHOOSE_START_GAME) {
			clearScreen();

			switch (DIFFICULTY) {
				case 1:
					nivelSelecionado = GAME_LEVEL_1;
					break;
				case 2:
					nivelSelecionado = GAME_LEVEL_2;
					break;
				case 3:
					nivelSelecionado = GAME_LEVEL_3;
					break;
				case 4:
					nivelSelecionado = GAME_LEVEL_4;
					break;
				case 5:
					nivelSelecionado = GAME_LEVEL_5;
					break;
				default:
					break;
			}

			state = new Game(MAX_SNAKE_SIZE, INITIAL_SNAKE_SIZE, mult);
			startGame();

		}
		else if(opcaoSelecionada == MAIN_MENU) {
			clearScreen();

			openMainMenu();
		}
		else if(opcaoSelecionada == QUIT_GAME) {
			exitGame();
		}
	}
	// Retornando a opcao selecionada no menu MULTIPLAYER
	private int opcaoMultiplayerMenu() {
		int selected  = CHOOSE_DIFFICULTY;

		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getKind()) {
					case ArrowDown:
						if(selected < QUIT_GAME) {
							if(selected >= CHOOSE_DIFFICULTY && selected < CHOOSE_TARGET_SCORE) {
								selected += 2;
							}else selected++;
						}

						break;

					case ArrowUp:
						if(selected > CHOOSE_DIFFICULTY)
							if(selected <= CHOOSE_TARGET_SCORE) {
								selected -= 2;
							}else selected--;

						break;
					case ArrowRight:
						if(selected == CHOOSE_DIFFICULTY && DIFFICULTY < 5) {
							DIFFICULTY++;
						}
						else if(selected == CHOOSE_WALLS && WALLS < 1) {
							WALLS++;
						}
						else if (selected == CHOOSE_TARGET_SCORE && TARGET_SCORE < 100) {
							TARGET_SCORE++;
						}
						break;
					case ArrowLeft:
						if(selected == CHOOSE_DIFFICULTY && DIFFICULTY > 1) {
							DIFFICULTY--;
						}
						else if(selected == CHOOSE_WALLS && WALLS > 0) {
							WALLS--;
						}
						else if (selected == CHOOSE_TARGET_SCORE && TARGET_SCORE > 1) {
							TARGET_SCORE--;
						}
						break;
					case Enter:
						if(selected == QUIT_GAME || selected == CHOOSE_START_GAME || selected == MAIN_MENU) {
							return selected;
						}
						else break;
					default:
						break;
				}

				representacaoGraficaMultiplayerMenu(selected);
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu MULTIPLAYER
	private void representacaoGraficaMultiplayerMenu(int selected) {

		drawString(5, 3, "Multiplayer Options:", Color.BLUE);
		drawString(5, 4, "- Difficulty: " + DIFFICULTY, Color.BLUE);
		drawString(5, 5, "- Walls: Yes No", Color.BLUE);
		drawString(5, 6, "- Target Score: " + TARGET_SCORE, Color.BLUE);
		drawString(5, 18, "Start Game", Color.BLUE);
		drawString(5, MAIN_MENU+3, "Back to main menu", Color.BLUE);
		drawString(5, QUIT_GAME+3, "Quit Game", Color.BLUE);

		if(selected == CHOOSE_DIFFICULTY) {
			drawString(5, 4, "- Difficulty: " + DIFFICULTY + "  ", Color.WHITE);
		}
		else if(selected == CHOOSE_WALLS) {
			if(WALLS == 0) {
				drawString(5, 5, "- Walls: Yes", Color.WHITE);
				drawString(18, 5, "No", Color.BLUE);
			}
			else if(WALLS == 1) {
				drawString(5, 5, "- Walls: ", Color.WHITE);
				drawString(14, 5, "Yes" , Color.BLUE);
				drawString(18, 5, "No", Color.WHITE);
			}
		}
		else if (selected == CHOOSE_TARGET_SCORE) {
			drawString(5, 6, "- Target Score: " + TARGET_SCORE + "  ", Color.WHITE);
		}
		else if (selected == CHOOSE_START_GAME) {
			drawString(5, 18, "Start Game", Color.WHITE);
		}
		else if(selected == MAIN_MENU) {
			drawString(5, 19, "Back to main menu", Color.WHITE);
		} else if(selected == QUIT_GAME) {
			drawString(5, 20, "Quit Game", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu MULTIPLAYER
	private void drawMultiplayerMenu() {
		int x = 5;
		int y = 2;

		drawString(x, y,   "######################################################################", Color.BLUE);
		drawString(x, ++y, "Multiplayer Options:", Color.BLUE);
		drawString(x, ++y, "- Difficulty: " + DIFFICULTY, Color.WHITE);
		drawString(x, ++y, "- Walls: Yes No", Color.BLUE);
		drawString(x, ++y, "- Target Score: " + TARGET_SCORE, Color.BLUE);
		drawString(x, ++y, "######################################################################", Color.BLUE);

		y += 10;

		drawString(x, y, "######################################################################", Color.BLUE);
		drawString(x, ++y,"Start Game", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.BLUE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "######################################################################", Color.BLUE);
	}

	// Cria o menu WIN MULTIPLAYER
	private void openWinMultiplayerMenu() {
		// Apaga todos os obstaculos e cerejas
		clearGameObjects();

		if(snakewin==1) {
			drawWinMultiplayerSnakeMenu();
		}else if(snakewin==2){
			drawWinMultiplayerSnakeMultiplayerMenu();
		}

		refreshScreen();

		int opcaoSelecionada = opcaoFinishMultiplayerMenu();

		if(opcaoSelecionada == MAIN_MENU) {
			clearScreen();

			openMainMenu();
		} else if(opcaoSelecionada == QUIT_GAME) {
			exitGame();
		}
	}
	// Cria o menu DRAW MULTIPLAYER
	private void openDrawMultiplayerMenu() {
		clearGameObjects();

		drawDrawMultiplayerMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoFinishMultiplayerMenu();

		if(opcaoSelecionada == MAIN_MENU) {
			clearScreen();

			openMainMenu();
		} else if(opcaoSelecionada == QUIT_GAME) {
			exitGame();
		}
	}
	// Retornando a opcao selecionada no menu WIN MULTIPLAYER
	private int opcaoFinishMultiplayerMenu() {
		int selected  = MAIN_MENU;

		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getKind()) {
					case ArrowDown:
						if(selected < QUIT_GAME)
							selected++;

						break;

					case ArrowUp:
						if(selected > MAIN_MENU)
							selected--;

						break;

					case Enter:
						return selected;

					default:
						break;
				}

				representacaoGraficaFinishMultiplayerMenu(selected);
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu WIN MULTIPLAYER
	private void representacaoGraficaFinishMultiplayerMenu(int selected) {

		drawString(28, MAIN_MENU, "Back to main menu", Color.BLUE);
		drawString(28, QUIT_GAME, "Quit Game", Color.BLUE);


		if(selected == MAIN_MENU) {
			drawString(28, MAIN_MENU, "Back to main menu", Color.WHITE);
		} else if(selected == QUIT_GAME) {
			drawString(28, QUIT_GAME, "Quit Game", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu WIN/DRAW MULTIPLAYER, menus diferentes dependendo do resultado
	private void drawWinMultiplayerSnakeMenu() {
		int x = 6;
		int y = 2;

		drawString(x, y, "######  ##        #########  ###    ###  ######## ########    ####", Color.CYAN);
		drawString(x,++y,"##  ##  ##        ###   ###  ###   ###   ##       ##   ##    ## ##", Color.CYAN);
		drawString(x,++y,"######  ##        #########   #######    #####    ######    ##  ##", Color.CYAN);
		drawString(x,++y,"##      ##        ###   ###     ###      ##       ##   ##       ##", Color.CYAN);
		drawString(x,++y,"##      ########  ###   ###     ###      ######## ##    ##      ##", Color.CYAN);

		x = 26;
		y++;

		drawString(x, ++y,"##        ##  ###  ###     ##", Color.CYAN);
		drawString(x, ++y,"##   ##   ##  ###  ## ##   ##", Color.CYAN);
		drawString(x, ++y,"## ##  ## ##  ###  ##  ##  ##", Color.CYAN);
		drawString(x, ++y,"####    ####  ###  ##   ## ##", Color.CYAN);
		drawString(x, ++y,"###      ###  ###  ##    ####", Color.CYAN);

		y++;
		y++;
		x = 28;

		drawString(x, ++y, "####################", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.WHITE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "####################", Color.BLUE);
	}
	private void drawWinMultiplayerSnakeMultiplayerMenu() {
		int x = 6;
		int y = 2;

		drawString(x, y, "######  ##        #########  ###    ###  ######## ########   ######", Color.CYAN);
		drawString(x,++y,"##  ##  ##        ###   ###  ###   ###   ##       ##   ##   ##   ##", Color.CYAN);
		drawString(x,++y,"######  ##        #########   #######    #####    ######        ##", Color.CYAN);
		drawString(x,++y,"##      ##        ###   ###     ###      ##       ##   ##     ###", Color.CYAN);
		drawString(x,++y,"##      ########  ###   ###     ###      ######## ##    ##   #######", Color.CYAN);

		x = 26;
		y++;

		drawString(x, ++y,"##        ##  ###  ###     ##", Color.CYAN);
		drawString(x, ++y,"##   ##   ##  ###  ## ##   ##", Color.CYAN);
		drawString(x, ++y,"## ##  ## ##  ###  ##  ##  ##", Color.CYAN);
		drawString(x, ++y,"####    ####  ###  ##   ## ##", Color.CYAN);
		drawString(x, ++y,"###      ###  ###  ##    ####", Color.CYAN);

		y++;
		y++;
		x = 28;

		drawString(x, ++y, "####################", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.WHITE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "####################", Color.BLUE);
	}
	private void drawDrawMultiplayerMenu() {
		int x = 19;
		int y = 7;

		drawString(x, y, "######   ########  #########  ##        ##", Color.CYAN);
		drawString(x,++y,"#     #  ##   ##   ###   ###  ##   ##   ##", Color.CYAN);
		drawString(x,++y,"#     #  ######    #########  ## ##  ## ##", Color.CYAN);
		drawString(x,++y,"#     #  ##   ##   ###   ###  ####    ####", Color.CYAN);
		drawString(x,++y,"######   ##    ##  ###   ###  ###      ###", Color.CYAN);

		y = 14;
		x = 28;

		drawString(x, ++y, "####################", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.WHITE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "####################", Color.BLUE);
	}

	// Cria o menu LEADERBOARD
	private void openLeaderboardMenu() {

		drawLeaderboardMenu();

		refreshScreen();

		int opcaoSelecionada = opcaoLeaderboardMenu();

		if(opcaoSelecionada == MAIN_MENU)
		{
			clearScreen();

			openMainMenu();
		}
		else if(opcaoSelecionada == QUIT_GAME)
		{
			exitGame();
		}
	}
	// Retornando a opcao selecionada no menu LEADERBOARD
	private int opcaoLeaderboardMenu() {
		int selected  = MAIN_MENU;

		Key k;

		while(true) {
			k = readKeyInput();

			if(k != null) {
				switch(k.getKind()) {
					case ArrowDown:
						if(selected < QUIT_GAME)
							selected++;

						break;

					case ArrowUp:
						if(selected > MAIN_MENU)
							selected--;

						break;

					case Enter:
						return selected;

					default:
						break;
				}

				representacaoGraficaLeaderboardMenu(selected);
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	// Representacao grafica do menu LEADERBOARD
	private void representacaoGraficaLeaderboardMenu(int selected) {

		drawString(5, MAIN_MENU+3, "Back to main menu", Color.BLUE);
		drawString(5, QUIT_GAME+3, "Quit Game", Color.BLUE);


		if(selected == MAIN_MENU) {
			drawString(5, MAIN_MENU+3, "Back to main menu", Color.WHITE);
		} else if(selected == QUIT_GAME) {
			drawString(5, QUIT_GAME+3, "Quit Game", Color.WHITE);
		}

		refreshScreen();
	}
	// Desenha o menu LEADERBOARD
	private void drawLeaderboardMenu() {
		int x = 5;
		int y = 2;

		drawString(x, y,   "######################################################################", Color.BLUE);
		drawString(x, ++y, "Leaderboard: ", Color.BLUE);
		++y;
		drawString(x, ++y, "Level 1 -", Color.BLUE);
		if(LEVEL_1_BESTTIME < Integer.MAX_VALUE) {
			drawString(x+9, y, " Player ", Color.CYAN);
			drawString(x+17, y, Integer.toString(LEVEL_1_BESTPLAYER), Color.CYAN);
			drawString(x+20, y, "Time - " + Long.toString(LEVEL_1_BESTTIME/1000) + '.' + Long.toString(LEVEL_1_BESTTIME%1000), Color.WHITE);
		}
		else drawString(x+9, y, " No Winners yet!", Color.BLUE);
		++y;
		drawString(x, ++y, "Level 2 -", Color.BLUE);
		if(LEVEL_2_BESTTIME < Integer.MAX_VALUE) {
			drawString(x+9, y, " Player ", Color.CYAN);
			drawString(x+17, y, Integer.toString(LEVEL_2_BESTPLAYER), Color.CYAN);
			drawString(x+20, y, "Time - " + Long.toString(LEVEL_2_BESTTIME/1000) + '.' + Long.toString(LEVEL_2_BESTTIME%1000), Color.WHITE);
		}
		else drawString(x+9, y, " No Winners yet!", Color.BLUE);
		++y;
		drawString(x, ++y, "Level 3 -", Color.BLUE);
		if(LEVEL_3_BESTTIME < Integer.MAX_VALUE) {
			drawString(x+9, y, " Player ", Color.CYAN);
			drawString(x+17, y, Integer.toString(LEVEL_3_BESTPLAYER), Color.CYAN);
			drawString(x+20, y, "Time - " + Long.toString(LEVEL_3_BESTTIME/1000) + '.' + Long.toString(LEVEL_3_BESTTIME%1000), Color.WHITE);
		}
		else drawString(x+9, y, " No Winners yet!", Color.BLUE);
		++y;
		drawString(x, ++y, "Level 4 -", Color.BLUE);
		if(LEVEL_4_BESTTIME < Integer.MAX_VALUE) {
			drawString(x+9, y, " Player ", Color.CYAN);
			drawString(x+17, y, Integer.toString(LEVEL_4_BESTPLAYER), Color.CYAN);
			drawString(x+20, y, "Time - " + Long.toString(LEVEL_4_BESTTIME/1000) + '.' + Long.toString(LEVEL_4_BESTTIME%1000), Color.WHITE);
		}
		else drawString(x+9, y, " No Winners yet!", Color.BLUE);
		++y;
		drawString(x, ++y, "Level 5 -", Color.BLUE);
		if(LEVEL_5_BESTTIME < Integer.MAX_VALUE) {
			drawString(x+9, y, " Player ", Color.CYAN);
			drawString(x+17, y, Integer.toString(LEVEL_5_BESTPLAYER), Color.CYAN);
			drawString(x+20, y, "Time - " + Long.toString(LEVEL_5_BESTTIME/1000) + '.' + Long.toString(LEVEL_5_BESTTIME%1000), Color.WHITE);
		}
		else drawString(x+9, y, " No Winners yet!", Color.BLUE);

		y=18;

		drawString(x, y, "######################################################################", Color.BLUE);
		drawString(x, ++y, "Back to main menu", Color.WHITE);
		drawString(x, ++y, "Quit Game", Color.BLUE);
		drawString(x, ++y, "######################################################################", Color.BLUE);
	}


	// Desenha a Snake
	private void drawSnake() {
		Position head = state.getSnakeCabeca();

		for(Position p : state.getSnakeCorpo()) {
			if(!p.equals(head)) {
				drawString(p.getX(), p.getY(), SNAKE_BODY_STRING, Color.GREEN);
			}
			else {
				drawString(p.getX(), p.getY(), SNAKE_HEAD_STRING, Color.GREEN);
			}
		}
	}
	private void drawSnakeMultiplayer() {
		Position head = state.getSnakeMultiplayerCabeca();

		for(Position p : state.getSnakeMultiplayerCorpo()) {
			if(!p.equals(head)) {
				drawString(p.getX(), p.getY(), SNAKE_BODY_STRING, Color.CYAN);
			}
			else {
				drawString(p.getX(), p.getY(), SNAKE_HEAD_STRING, Color.CYAN);
			}
		}
	}

	// Desenha o Score atual
	private void drawScore() {
		int s = state.getScore();
		drawString(10, gameplay_height + 1, Integer.toString(s), null);
	}
	private void drawScoreMultiplayer() {
		int s = state.getScoreMultiplayer();
		drawString(75, gameplay_height + 1, Integer.toString(s), null);
	}

	// Apaga todas as cerejas e obstaculos
	private void clearGameObjects() {
		for(Position p : state.getCerejas()) {
			clearStringAt(p.getX(), p.getY());
		}

		for(Position p : state.getObstaculos()) {
			clearStringAt(p.getX(), p.getY());
		}

		for(Position p : state.getCerejasBonus()) {
			clearStringAt(p.getX(), p.getY());
		}

		for(Position p : state.getSnakeCorpo()) {
			clearStringAt(p.getX(), p.getY());
		}

		if(mult==true){
			for(Position p : state.getSnakeMultiplayerCorpo()) {
				clearStringAt(p.getX(), p.getY());
			}
		}
	}

	// Desenha uma nova cereja
	private void generateNovaCereja() {
		Position p = state.generateRandomObject(gameplay_width, gameplay_height);

		state.getCerejas().add(new Position(p.getX(), p.getY()));
		drawString(p.getX(), p.getY(), CEREJA_STRING, Color.RED);
	}
	// Desenha uma nova cereja Bonus
	private void generateNovaCerejaBonus() {
		Position p = state.generateRandomObject(gameplay_width, gameplay_height);

		state.getCerejasBonus().add(new Position(p.getX(), p.getY()));
		drawString(p.getX(), p.getY(), CEREJABONUS_STRING, Color.BLUE);
	}
	// Desenha um novo obstaculo
	private void generateNovoObstaculo() {
		Position p = state.generateRandomObject(gameplay_width, gameplay_height);

		state.getObstaculos().add(new Position(p.getX(), p.getY()));
		drawString(p.getX(), p.getY(), OBSTACULO_STRING, Color.YELLOW);
	}

	// Mostra a posicao onde a colisao aconteceu
	private void mostraCrashPosition(int x, int y) {
		drawString(x, y, "X", Color.RED);
	}

	// Retorna o input dado pelo keyboard
	private Key readKeyInput() {
		return terminal.readInput();
	}

	// Faz com que as alteracoes feitas sejam visiveis
	private void refreshScreen() {
		screen.refresh();
	}

	// Apaga todas os objetos do screen
	private void clearScreen() {
		screen.clear();
	}

	// Sai do PrivateMode entrado anteriormente
	private void exitGame() {
		terminal.exitPrivateMode();
	}
}
