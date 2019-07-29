package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api/")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

   @RequestMapping("/games")
    //Hago un listado de games
    /*public List<Map<String,Object>> getAllGameId() {
        return gameRepository.findAll().stream().map(Game::makeGameDTO).collect(Collectors.toList());


        }*/
   public Map<String, Object> getGames (Authentication authentication){
       Map<String,Object> dto = new HashMap<>();
       if(!this.isGuest(authentication))
           dto.put("player", playerRepository.findByUserName(authentication.getName()).makeGameDTO_Player());
       else
           dto.put("player", "Guest");
       dto.put("games", gameRepository.findAll().stream().map(Game::makeGameDTO).collect(Collectors.toList()));
       return dto;
   }


    @RequestMapping("/game_view/{gamePlayerid}")
    //Hago un listado de gamePlayer ID
    public Map<String,Object> getGameView(@PathVariable Long gamePlayerid) {
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerid);
        if(gamePlayer.isPresent())
           return gamePlayer.get().dto_gameView();
        else
            return null;
    }
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)

    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String playerName, //@RequestParam String playerLastName,
            @RequestParam String username, @RequestParam String password) {

        if (username.isEmpty()) {
            return new ResponseEntity<>(makeMap("error","No name"), HttpStatus.FORBIDDEN);
        }
Player player = playerRepository.findByUserName(username);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error","Name already exists"), HttpStatus.FORBIDDEN);
        }
Player newPlayer = playerRepository.save(new Player(playerName, /*playerLastName,*/ username, passwordEncoder.encode(password)));

        return new ResponseEntity<>(makeMap("id", newPlayer.getPlayerId()), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> createGame(Authentication authentication, @RequestParam String side) {
    ResponseEntity<Object> response;
        if (!authentication.isAuthenticated()) {
            response = new ResponseEntity<>(makeMap("error", "No authenticated"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByUserName(authentication.getName());
        if (player != null) {
            if( !side.equals("VILLAIN") && !side.equals("HERO")){
                response = new ResponseEntity<>("Wrong Team", HttpStatus.FORBIDDEN);
            }else {
                Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
                GamePlayer newGamePlayer;
                if(side.equals("VILLAIN")){
                    newGamePlayer =  new GamePlayer(LocalDateTime.now(), newGame, player, Side.VILLAIN);
                } else{
                    newGamePlayer =  new GamePlayer(LocalDateTime.now(), newGame, player, Side.HERO);
                }

                gamePlayerRepository.save(newGamePlayer);
                response = new ResponseEntity<>(makeMap("gpid", newGamePlayer.getGamePlayerId()), HttpStatus.CREATED);

            }


        }
        else{
            response = new ResponseEntity<>(makeMap("error", "No User Found"), HttpStatus.FORBIDDEN);
        }
        return response;
   }


    @RequestMapping(path = "/game/{game_id}/players", method = RequestMethod.POST)
    @ResponseBody

    public ResponseEntity<Object> joinGame(@PathVariable Long game_id, Authentication authentication, @RequestParam String side) {
        ResponseEntity<Object> response;
        if (isGuest(authentication)) {
            response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            Optional<Game> OptionalGame = gameRepository.findById(game_id);
             Game game = new Game();
            if (OptionalGame.isPresent()) {
                game = OptionalGame.get();
                if (game.getGamePlayers().stream().count() > 1) {
                    response = new ResponseEntity<>("The game is already full", HttpStatus.FORBIDDEN);
                } else if( !side.equals("VILLAIN") && !side.equals("HERO")){
                    response = new ResponseEntity<>("Wrong Team", HttpStatus.FORBIDDEN);
                }
                else {
                    Player player = playerRepository.findByUserName(authentication.getName());
                    GamePlayer gamePlayer;
                    if(side.equals("VILLAIN")){
                       gamePlayer =  new GamePlayer(LocalDateTime.now(), game, player, Side.VILLAIN);
                    } else{
                        gamePlayer =  new GamePlayer(LocalDateTime.now(), game, player, Side.HERO);
                    }

                    gamePlayerRepository.save(gamePlayer);
                    response = new ResponseEntity<>(makeMap("gpid", gamePlayer.getGamePlayerId()), HttpStatus.CREATED);
                }
            }
            else {
                response = new ResponseEntity<>("No such Game", HttpStatus.FORBIDDEN);
            }
        }
        return response;
    }

    @RequestMapping(path = "/games/players/{gamePlayerid}/ships", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> addShips(@PathVariable long gamePlayerid, Authentication authentication, @RequestBody List<Ship> ships) {
        Optional<GamePlayer> optGamePlayer = gamePlayerRepository.findById(gamePlayerid);
        ResponseEntity<Object> responseEntity;
        Player currentUser = playerRepository.findByUserName(authentication.getName());

        if (this.isGuest(authentication)) {
            responseEntity = new ResponseEntity<>("No game player created", HttpStatus.FORBIDDEN);
        } else if (!optGamePlayer.isPresent()) {
            responseEntity = new ResponseEntity<>("Not logged", HttpStatus.FORBIDDEN);
        } else if (optGamePlayer.get().getPlayer().getPlayerId() != currentUser.getPlayerId()) {
            responseEntity = new ResponseEntity<>("Reached limit of salvoes", HttpStatus.FORBIDDEN);
        } else if (optGamePlayer.get().getShips().size() > 0) {
            responseEntity = new ResponseEntity<>("This player has already place ships", HttpStatus.FORBIDDEN);
        } else {
            GamePlayer gamePlayer = optGamePlayer.get();
           ships.stream().forEach((ship ->{gamePlayer.addShip(ship);}));

            gamePlayerRepository.save(gamePlayer);

            responseEntity = new ResponseEntity<>("Ships created", HttpStatus.CREATED);

        }
        return responseEntity;
    }

    @RequestMapping(path = "/games/players/{gamePlayerid}/salvoes", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> addSalvoes(@PathVariable long gamePlayerid, Authentication authentication, @RequestBody List<String> locations) {
        Optional<GamePlayer> optGamePlayer = gamePlayerRepository.findById(gamePlayerid);
        ResponseEntity<Object> responseEntity;
        Player currentUser = playerRepository.findByUserName(authentication.getName());

        if(this.isGuest(authentication)){
        responseEntity = new ResponseEntity<>("No game player created", HttpStatus.FORBIDDEN);
        } else if(!optGamePlayer.isPresent()){
            responseEntity =  new ResponseEntity<>("Not logged", HttpStatus.FORBIDDEN);
        }else if(locations.size()>5){
                responseEntity = new ResponseEntity<>("Reached limit of salvoes", HttpStatus.FORBIDDEN);
        }else if (optGamePlayer.get().getPlayer().getPlayerId() != currentUser.getPlayerId()) {
            responseEntity = new ResponseEntity<>("You cannot see your opponent's salvoes", HttpStatus.FORBIDDEN);
        }else{
            GamePlayer gamePlayer = optGamePlayer.get();
            int turn = gamePlayer.getSalvoes().size()+1;
            Salvo salvo = new Salvo(turn, locations);
            gamePlayer.addSalvo(salvo);
            gamePlayerRepository.save(gamePlayer);

            responseEntity = new ResponseEntity<>("salvos were stored  succesfully", HttpStatus.CREATED);

        }
        return responseEntity;
    }


    private Map<String, Object>makeMap(String key, Object value){
        Map<String, Object> map = new HashMap<>();
        map.put(key,value);
        return map;
    }
}