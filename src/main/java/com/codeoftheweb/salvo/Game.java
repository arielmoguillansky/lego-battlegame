package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime gameDate;

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet();

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<Score> scores = new HashSet();

    public Game() { }

    //constructor

    public Game(LocalDateTime gameDate){
        this.gameDate = gameDate;
    }

    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDateTime gameDate) {
        this.gameDate = gameDate;
    }

    public long getGameId(){
        return id;
    }

    public List<Player> getPlayer() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

    public Set<GamePlayer> getGamePlayers(){
        return gamePlayers;
    }

    public Set<Score> getScore() {
        return scores;
    }

    public void setScore(Set<Score> scores) {
        this.scores = scores;
    }

    //Creo un Map, y dentro un Linked HashMap que inserta los objetos en el orden que se han introducido
    public Map<String, Object> makeGameDTO (){
        Map<String, Object> gameDTO = new LinkedHashMap<>(); //We use a LinkedHashMap so that we can control the order of keys in the map.
        gameDTO.put("id", this.getGameId());
        gameDTO.put("created", this.getGameDate());
        gameDTO.put("gamePlayers", this.getGamePlayers().stream().map(GamePlayer::makeGameDTO_gamePlayer));
        return gameDTO;
    }

    public void addGamePlayer(GamePlayer gameplayer){
        gameplayer.setGame(this);
        gamePlayers.add(gameplayer);
    }


}
