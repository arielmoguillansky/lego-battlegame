package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;
    private String playerName;
    private String playerLastName;
    private String password;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    Set<Score> scores = new HashSet();

    public Player() { }

    public Player(String playerN, String user,  String pass) {
        this.userName = user;
        this.playerName = playerN;
        //this.playerLastName = playerL;
        this.password = pass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerLastName() {
        return playerLastName;
    }

    public void setPlayerLastName(String playerLastName) {
        this.playerLastName = playerLastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return playerName + " " + " " + playerLastName + " " + userName + id;
    }

    public List<Game> getGame() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Score getScore(Game game){
        return scores.stream().filter(score->score.getGame().getGameId() == game.getGameId()).findAny().orElse(null);
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public long getPlayerId(){return id;}

    public Map<String, Object> makeGameDTO_Player (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getPlayerId());
        dto.put("user", this.getUserName());
        dto.put("user_name", this.getPlayerName());
        return dto;
    }

    public void addGamePlayer(GamePlayer gameplayer){
        gameplayer.setPlayer(this);
        gamePlayers.add(gameplayer);
    }

}
