
## Trabalho de Laboratório de Desenho e Teste de Software - Snake Game

Neste trabalho pretende-se desenvolver o "famoso" Jogo da Snake. O jogo consiste numa cobra que ao comer 
cerejas distribuidas pelo campo de jogo aumenta de tamanho e assim tenta completar o nível em que se encontra
tentando desviar-se das paredes, dos obstáculos e tentando evitar chocar contra o seu próprio corpo.

Este projeto foi desenvolvido por Rafael Alves (up202004476@fc.up.pt), Francisco Teixeira 
(up202006111@fc.up.pt) e Manuel Alves (up201906910@fe.up.pt) para LDTS 2021/2022.

### IMPLEMENTED FEATURES

**Aumentar de Tamanho** - A snake aumenta de tamanho quando apanha uma cereja                                   
**Perder** - Se a snake embater na sua própria cauda ou contra um obstáculo/parede perde                  
**Velocidade** - A velocidade de movimento da snake aumenta à medida que sobe de nível                 
**Aumentar Dobro Tamanho** - Existência de uma "cereja bónus" que conta como se fossem duas cerejas  
**Passar Nível** - Se snake apanhar X cerejas completa o nível e passa para o seguinte  
**Menu Melhores Pontuações** - Existência de uma classificação global para as melhores pontuações que são guardadas após o
final de cada jogo   
**Criação de Níveis** - Um jogador pode criar um nível definindo o número de cerejas no nível
assim como o nível de dificuldade, o tamanho inicial da cobra e o tamanho maximo  
**Multiplayer** - Haver um modo em que são permitidos dois jogadores em simultâneo, nesse modo ganha
quem chegar primeiro a um X número de cerejas  
**Ganhar Multiplayer** - Se um jogador embater contra outro jogador perde, tornando o outro jogador o vencedor

![mainMenu](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/mainMenu.PNG)
![tamanhoeScoreAumenta](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/tamanhoeScoreAumenta.PNG)
![perdeEmbaterObstaculo](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/perdeEmbaterObstaculo.PNG)
![levelWinMenu](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/levelWinMenu.PNG)
![multiplayerJogo](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/multiplayerJogo.PNG)
![menuWinmultiplayer](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/menuWinmultiplayer.PNG)
![menuCreative](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/menuCreative.PNG)
![leaderboardMenu](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/leaderboardMenu.PNG)
![instrucoesJogo](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/instrucoesJogo.PNG)

### PLANNED FEATURES

Todas as "Planned Features" foram implementadas com sucesso.

### DESIGN

#### O movimento da cobra deve variar consoante a tecla que seja pressionado no teclado dependendo da direcao atual

**Problem in Context**

O problema foi perceber como fazer com que a Snake se movimenta-se na mesma direção que a tecla
pressionada pelo utilizador no teclado.

**The Pattern**

Nós aplicamos o **State** pattern. Este pattern permite que o objeto altere o seu comportamento
quando o seu estado interno muda (tecla do teclado é pressionada).

**Implementation**

![state](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/state.png)

**Consequences**

O uso do de State pattern no design atual permite que a habilidade da Snake mudar de direção
torna-se explícita no código.

#### O menu deve responder consoante a tecla pressionada no teclado

**Problem in Context**

O problema consiste em associar a ação de pressionar uma tecla, ao código que contem a ação que lhe corresponde.

**The Pattern**

Aplicamos o **Command** pattern. Este pattern permite-nos transmitir a informação sobre a tecla que é pressionada quando o utilizador se encontra no menu para o código que, ao receber essa informação executa o código tendo como base a informação recolhida.

**Implementation**

![command](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/command.png)

**Consequences**

The use of the State Pattern in the current design allows the following benefits:

- Permite que ao carregar na tecla 's' no menu inicial, o jogador comece o jogo.
- Permite que ao carregar na tecla 'i' no menu inicial, o jogador seja direcionado para as instruções do jogo.
- Permite que ao carregar na tecla 'q' no menu inicial, a janela do jogo seja fechada.
- Permite que ao movimentar-se através dos números '1' a '5', o utilizador possa selecionar o nível que pretende jogar.

#### Haver classe para implementação e abstração

**Problem in Context**

O problema foi dividir uma grande classe em duas classes, uma para implementação e outra para abstração.

**The Pattern**

Nós aplicamos o **Bridge** pattern. Este pattern permite dividir uma grande classe intimamente relacionadas 
em duas hierarquias separadas - abstração e implementação - que podem ser desenvolvidas independentemente uma da outra.

**Implementation**

![bridge](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/bridge.png)

**Consequences**

O uso do de Bridge pattern no design atual permite que haja uma ligação entre Game e GameBuild,
sem a necessidade de existir uma grande classe com o código todo dentro dela.

#### O programa deve avaliar a posição da snake

**Problem in Context**

O problema consiste em saber a posição da cobra com o intuito de avaliar se a mesma se encontra na mesma posição que uma parede, obstáculo ou ela mesma, o que provoca a sua morte.

**The Pattern**

Aplicamos o **Observer** pattern. Este pattern permite-nos saber a posição da cobra e notificar caso se encontre na mesma posição dos outros objetos que poderão causar a sua morte e assim terminar o nível/jogo.

**Implementation**

![observer](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/Observer.png)

**Consequences**

The use of the Observer Pattern in the current design allows the following benefits:

- Permite que assim que a cabeça da cobra se encontre na mesma posiçao que um obstáculo o jogo acabe.
- Permite que assim que a cabeça da cobra se encontre na mesma posiçao que uma parede o jogo acabe.
- Permite que assim que a cabeça da cobra se encontre na mesma posiçao que uma parte do seu corpo o jogo acabe.

#### O programa deve poder fazer alterações utilizando em conjunto várias classes entre os ficheiros do jogo

**Problem in Context**

O problema consiste em poder alterar a snake existindo uma ligação entre os ficheiros e classes.

**The Pattern**

Aplicamos o **Strategy** pattern. Este pattern permite-nos fazer alterações na snake, a partir de um ficheiro que cria a snake e a altera utilizando classes de um outro ficheiro.

**Implementation**

![strategy](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/Strategy.png)

**Consequences**

The use of the Strategy Pattern in the current design allows the following benefits:

- Permite que o corpo da cobra seja criado com um tamanho inicial.
- Permite que a snake mude de tamanho consoante os acontecimentos do jogo.
- Permite mudar a velocidade da snake consoante o nível.

### CODE SMELLS AND REFACTORING

#### **Large Class**

A classe GameBuild contém muitos métodos. Neste caso, nós achamos justificável pois esta classe é a principal classe do programa e precisa de guardar uma grande quantidade de dados e necessita de vários métodos para a interface do jogo. No entanto, para ajudar a perceber mais facilmente esta classe, organizamos os métodos por "secções".  
Uma possível solução para este smell code seria aplicar o refactoring **Extract Class**, criando, por exemplo, uma classe para os métodos dos diferentes menus.  

#### **Long Method**

O método updateGame() (classe GameBuild) contém muitas linhas de código, pois é onde se passa a parte mais importante do jogo, em que temos de fazer diversas comparações no que toca a colisões, movimentos, fim de jogo, etc.
Uma possível solução para este smell code seria aplicar o refactoring **Extract Method**, dividindo o método em várias partes.

#### **Duplicate Code**

O método updateGame() (classe GameBuild) contém código idêntico, para verificar o estado da Snake quando o jogo se encontra no modo Single Player ou Multiplayer.  
Uma possível solução para este smell code seria aplicar o refactoring **Extract Method**, dividindo o método em dois métodos, consoante nos encontramos no Single Player e num Multiplayer.

#### **Switch Statements**

O método readKeyboard (classe GameBuild) contém um complexo switch operator, para definir o movimento da(s) Snake(s) no jogo.  
Nós achamos este smell code justificável pois era a maneira mais prática de avaliar o movimento da(s) Snake(s).

#### **Alternative Classes with Different Interfaces**

As classes Snake e SnakeMultiplayer são idênticas, realizando as mesmas funções, só que para Snakes diferentes.  
Uma solução seria criar uma classe geral que conseguissemos aplicar às duas Snakes diferentes.

### TESTING

#### **Screenshot of coverage report**

![SnakeTest](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/SnakeTest.jpg)

#### **Link to mutation testing report and screenhot**

[Mutation Report](/Users/nelinho_8/Desktop/snake/build/reports/pitest/202201251340/index.html)  
![MutationReport](https://github.com/FEUP-LDTS-2021/ldts-project-assignment-g0302/blob/main/docs/screenshots/PITEST.png)

### SELF-EVALUATION

O trabalho foi dividido de maneira igual por todos os elementos do grupo, e toda a gente contribuiu para o mesmo.   
Este trabalho ajudou-nos a enriquecer os nossos conhecimentos em java, patterns e smell codes, tal como o nosso trabalho em equipa.

 - Francisco Teixeira: 33.3%
 - Manuel Alves: 33.3%
 - Rafael Alves: 33.3%
