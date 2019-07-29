package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private long turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvo_location")
    private List<String> salvoLocations = new ArrayList<>();

    public Salvo() {
    }

    public Salvo(long turn, List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
        this.turn = turn;
    }


    public long getTurn() {
        return turn;
    }

    public void setTurn(long turn) {
        this.turn = turn;
    }

       public GamePlayer getGamePlayer() {
           return gamePlayer;
       }

       public void setGamePlayer(GamePlayer gamePlayer) {
           this.gamePlayer = gamePlayer;
       }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    private List<String> getHitShips(){
        List<String> hitShips = new LinkedList<>();
        if (this.getEnemyGamePlayer() != null)

            hitShips = this.getSalvoLocations().stream().filter(location -> getEnemyGamePlayer().getShips().stream().anyMatch(ship -> ship.getLocations().contains(location))).collect(Collectors.toList());

        else
            hitShips = null;
        return hitShips;
    }

    private List<Map<String,Object>> getSinkShips(){
        List<Map<String,Object>> sinkShips = new LinkedList<>();
        List<String> allEnemyLoc = new ArrayList<>();
        this.getGamePlayer().getSalvoes().stream().filter(e -> e.getTurn()<= this.getTurn()).forEach(x -> allEnemyLoc.addAll(x.getSalvoLocations()));
        if (this.getEnemyGamePlayer() != null)

            sinkShips = this.getEnemyGamePlayer().getShips().stream().filter(ship -> allEnemyLoc.containsAll(ship.getLocations())).map(Ship::makeGameDTO_Ship).collect(Collectors.toList());

        else
            sinkShips = null;
        return sinkShips;
    }

    private GamePlayer getEnemyGamePlayer(){
        GamePlayer enemyGamePlayer = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getGamePlayerId() != this.getGamePlayer().getGamePlayerId()).findFirst().orElse(null);
        return enemyGamePlayer;
    }

    public Map<String, Object> makeGameDTO_Salvo (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getUserName());
        dto.put("game_player_id", this.getGamePlayer().getGamePlayerId());
        dto.put("locations", this.getSalvoLocations());
        dto.put("hits", this.getHitShips());
        dto.put("sinks", this.getSinkShips());
        return dto;
    }
}