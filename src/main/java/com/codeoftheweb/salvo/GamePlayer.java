package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime gamePlayerDate;
    private Side side;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Ship>ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Salvo>salvoes = new HashSet<>();


    public GamePlayer() { }

    public GamePlayer(LocalDateTime gamePlayerDate , Game game, Player player, Side side) {
        this.game = game;
        this.player = player;
        this.gamePlayerDate = gamePlayerDate;
        this.side = side;
    }

    //constructor
    public GamePlayer(LocalDateTime gamePlayerDate){
        this.gamePlayerDate = gamePlayerDate;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public LocalDateTime getGamePlayerDate() {
        return gamePlayerDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getGamePlayerId(){return id; }

    public long getGameId(){
        return id;
    }

    public Set<Ship> getShips(){
        return ships;
    }

    public Set<Salvo> getSalvoes(){
        return salvoes;
    }


    public Map<String, Object> dto_gameView (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getGameId());
        dto.put("created", this.getGame().getGameDate());
        dto.put("gamePlayers", this.game.getGamePlayers().stream().map(GamePlayer::makeGameDTO_gamePlayer));
        dto.put("ships", this.getShips().stream().map(Ship::makeGameDTO_Ship));
        dto.put("salvoes", this.game.getGamePlayers().stream().flatMap(gamePlayer->gamePlayer.getSalvoes().stream().map(Salvo::makeGameDTO_Salvo)));
        dto.put("gameState", this.gameState());
        return dto;
    }

    public Map<String, Object> makeGameDTO_gamePlayer (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gpid", this.getGamePlayerId());
        dto.put("Players", this.getPlayer().makeGameDTO_Player());
        dto.put("side", this.getSide());
        if(this.getPlayer().getScore(this.getGame()) != null)
            dto.put("score", this.getPlayer().getScore(this.getGame()).getScore());
        else dto.put("score", null);
        return dto;
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    private GamePlayer getEnemyGamePlayer(){
        GamePlayer enemyGamePlayer = this.getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getGamePlayerId() != this.getGamePlayerId()).findFirst().orElse(null);
        return enemyGamePlayer;
    }

    private long getGamePlayerSink(int turn, Set <Salvo> salvo, Set <Ship> ships){
        List<String> shoot = new LinkedList<>();
        List<String> allEnemyLoc = new ArrayList<>();
        long sinks = 0;


        salvo.stream().filter(e -> e.getTurn()<= turn).forEach(x -> shoot.addAll(x.getSalvoLocations()));

        sinks = ships.stream().filter(s ->shoot.containsAll(s.getLocations())).count();


           // shoot = this.getEnemyGamePlayer().getShips().stream().filter(ship -> allEnemyLoc.containsAll(ship.getLocations())).map(Ship::makeGameDTO_Ship).collect(Collectors.toList());

        return sinks; //retorna n° de sinks
    }



    private String gameState() {

        String gameState="";
        int turn = this.getSalvoes().size() + 1;
        int enemyTurn;
        long mysinks;
        long enemySinks;
        if(this.getEnemyGamePlayer() != null) {
            enemyTurn = this.getEnemyGamePlayer().getSalvoes().size() + 1;
            mysinks = this.getGamePlayerSink(turn, this.getSalvoes(), this.getEnemyGamePlayer().getShips());
            enemySinks = this.getGamePlayerSink(turn, this.getEnemyGamePlayer().getSalvoes(), this.getShips());


            if (this.getGamePlayerId() < this.getEnemyGamePlayer().getGamePlayerId()) {
                if(turn > enemyTurn){
                gameState = "wait";
            }else if(turn == enemyTurn) {
                    if (this.getShips().size() < 5) {
                        gameState = "place ships"; //place ships
                    } else if (this.getShips().size() == 5 && this.getEnemyGamePlayer().getShips().size() < 5) {
                        gameState = "wait opponent ships"; //wait for enemy ships
                    } else {
                        gameState = "shoot";
                    }
                }
            }
            if (this.getGamePlayerId() > this.getEnemyGamePlayer().getGamePlayerId()) {
                if(turn < enemyTurn){
                    gameState = "shoot";
                }else if(turn == enemyTurn) {
                    if (this.getShips().size() < 5) {
                        gameState = "place ships"; //place ships
                    } else if (this.getShips().size() == 5 && this.getEnemyGamePlayer().getShips().size() < 5) {
                        gameState = "wait opponent ships"; //wait for enemy ships
                    } else {
                        gameState = "wait";
                    }
                }
            }
            if (mysinks == 5 && turn == enemyTurn) {
                gameState = "win"; //G.O Win
            } else if (enemySinks == 5 && turn == enemyTurn) {
                gameState = "lose"; //G.O Lose
            } else if (mysinks == 5 && enemySinks == 5 && enemyTurn == turn) {
                gameState = "tie"; //G.O tie
            }

        }else{ enemyTurn = 0;
            mysinks = 0;
            enemySinks = 0;

            if (this.getShips().size() < 5) {
                gameState = "place ships"; //place ships
            }else if (this.getShips().size() == 5 && this.getEnemyGamePlayer() == null) {
                gameState = "wait opponent"; //wait for enemy ships
            }
        }
        return gameState;
    }
}

