package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);

	}

	@Autowired
	PasswordEncoder passwordEncoder;





	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {

			Player player1 = new Player("Jack", /*Bauer,*/ "j.bauer@ctu.gov", passwordEncoder.encode("24"));
			Player player2 = new Player("Chloe", "c.obrian@ctu.gov", passwordEncoder.encode("42"));
			Player player3 = new Player("Tony", "t.almeida@ctu.gov", passwordEncoder.encode("mole"));
			Player player4 = new Player("Kim", "kim_bauer@gmail.com", passwordEncoder.encode("kb"));

			List<String> locations1 = new ArrayList(Arrays.asList("H2", "H3","H4","H5","H6"));
			List<String> locations2 = new ArrayList(Arrays.asList("E1", "F1", "G1"));
			List<String> locations3 = new ArrayList(Arrays.asList("B4", "B5"));
			List<String> locations4 = new ArrayList(Arrays.asList("B5", "C5", "D5"));
			List<String> locations5 = new ArrayList(Arrays.asList("F1","F2","F3","F4"));
			List<String> locations6 = new ArrayList(Arrays.asList("C6", "C7"));
			List<String> locations7 = new ArrayList(Arrays.asList("A2", "A3", "A4"));
			List<String> locations8 = new ArrayList(Arrays.asList("G6","H6"));

			Ship ship1_1_1 = new Ship(ShipType.IRONMAN, locations1);
			Ship ship2_1_1 = new Ship(ShipType.DRSTRANGE, locations2);
			Ship ship3_1_1 = new Ship(ShipType.ROCKET, locations3);
			Ship ship1_1_2 = new Ship(ShipType.SPIDERMAN, locations4);
			Ship ship2_1_2 = new Ship(ShipType.CPAMERICA, locations5);

			Ship ship1_2_1 = new Ship(ShipType.ROCKET, locations6);
			Ship ship2_2_1 = new Ship(ShipType.DRSTRANGE, locations7);
			Ship ship1_2_2 = new Ship(ShipType.ROCKET, locations8);
			Ship ship2_2_2 = new Ship(ShipType.ROCKET, locations8);

			Ship ship1_3_1 = new Ship(ShipType.ROCKET, locations6);
			Ship ship2_3_1 = new Ship(ShipType.DRSTRANGE, locations7);
			Ship ship1_3_2 = new Ship(ShipType.ROCKET, locations8);
			Ship ship2_3_2 = new Ship(ShipType.ROCKET, locations8);

			Ship ship1_4_1 = new Ship(ShipType.ROCKET, locations6);
			Ship ship2_4_1 = new Ship(ShipType.DRSTRANGE, locations7);
			Ship ship1_4_2 = new Ship(ShipType.ROCKET, locations8);
			Ship ship2_4_2 = new Ship(ShipType.ROCKET, locations8);

			Ship ship1_5_1 = new Ship(ShipType.ROCKET, locations6);
			Ship ship2_5_1 = new Ship(ShipType.DRSTRANGE, locations7);
			Ship ship1_5_2 = new Ship(ShipType.ROCKET, locations8);
			Ship ship2_5_2 = new Ship(ShipType.ROCKET, locations8);

			Ship ship1_6_1 = new Ship(ShipType.ROCKET, locations6);
			Ship ship2_6_1 = new Ship(ShipType.DRSTRANGE, locations7);
			Ship ship1_6_2 = new Ship(ShipType.SPIDERMAN, locations4);
			Ship ship2_6_2 = new Ship(ShipType.ROCKET, locations5);

			Ship ship1_7_1 = new Ship(ShipType.ROCKET, locations6);
			Ship ship2_7_1 = new Ship(ShipType.DRSTRANGE, locations7);
			Ship ship1_7_2 = new Ship(ShipType.SPIDERMAN, locations4);
			Ship ship2_7_2 = new Ship(ShipType.ROCKET, locations5);

			Ship ship1_8_1 = new Ship(ShipType.ROCKET, locations8);
			Ship ship2_8_1 = new Ship(ShipType.ROCKET, locations8);
			Ship ship1_8_2 = new Ship(ShipType.ROCKET, locations8);
			Ship ship2_8_2 = new Ship(ShipType.ROCKET, locations8);

			List<String> salvoLocations1 = new ArrayList<>(Arrays.asList("B5", "C5", "F1"));
			List<String> salvoLocations2 = new ArrayList<>(Arrays.asList("B4", "B5","B6"));
			List<String> salvoLocations3 = new ArrayList<>(Arrays.asList("F2", "D5"));
			List<String> salvoLocations4 = new ArrayList<>(Arrays.asList("E1", "H3","A2"));
			List<String> salvoLocations5 = new ArrayList<>(Arrays.asList("A2", "A4","A6"));
			List<String> salvoLocations6 = new ArrayList<>(Arrays.asList("B5", "D5","C7"));
			List<String> salvoLocations7 = new ArrayList<>(Arrays.asList("A3", "H6"));
			List<String> salvoLocations8 = new ArrayList<>(Arrays.asList("C5", "C6"));
			List<String> salvoLocations9 = new ArrayList<>(Arrays.asList("G6", "H6", "A4"));
			List<String> salvoLocations10 = new ArrayList<>(Arrays.asList("H1", "H2", "H3"));
			List<String> salvoLocations11 = new ArrayList<>(Arrays.asList("A2", "A3", "D8"));
			List<String> salvoLocations12 = new ArrayList<>(Arrays.asList("E1", "F2", "G3"));
			List<String> salvoLocations13 = new ArrayList<>(Arrays.asList("A3", "A4", "F7"));
			List<String> salvoLocations14 = new ArrayList<>(Arrays.asList("B5", "C6", "H1"));
			List<String> salvoLocations15 = new ArrayList<>(Arrays.asList("A2", "G6", "H6"));
			List<String> salvoLocations16 = new ArrayList<>(Arrays.asList("C5", "C7", "D5"));
			List<String> salvoLocations17 = new ArrayList<>(Arrays.asList("A1", "A2", "A3"));
			List<String> salvoLocations18 = new ArrayList<>(Arrays.asList("B5", "B6", "C7"));
			List<String> salvoLocations19 = new ArrayList<>(Arrays.asList("G6", "G7", "G8"));
			List<String> salvoLocations20 = new ArrayList<>(Arrays.asList("C6", "D6", "E6"));
			List<String> salvoLocations21 = new ArrayList<>(Arrays.asList("H1", "H8"));

			Salvo salvo1_1_1 = new Salvo(1,salvoLocations1);
			Salvo salvo1_1_2 = new Salvo(1,salvoLocations2);
			Salvo salvo1_2_1 = new Salvo(2,salvoLocations3);
			Salvo salvo1_2_2 = new Salvo(2,salvoLocations4);
			Salvo salvo2_1_1 = new Salvo(1,salvoLocations5);
			Salvo salvo2_1_2 = new Salvo(1,salvoLocations6);
			Salvo salvo2_2_1 = new Salvo(2,salvoLocations7);
			Salvo salvo2_2_2 = new Salvo(2,salvoLocations8);
			Salvo salvo3_1_1 = new Salvo(1,salvoLocations9);
			Salvo salvo3_1_2 = new Salvo(1,salvoLocations10);
			Salvo salvo3_2_1 = new Salvo(2,salvoLocations11);
			Salvo salvo3_2_2 = new Salvo(2,salvoLocations12);
			Salvo salvo4_1_1 = new Salvo(1,salvoLocations13);
			Salvo salvo4_1_2 = new Salvo(1,salvoLocations14);
			Salvo salvo4_2_1 = new Salvo(2,salvoLocations15);
			Salvo salvo4_2_2 = new Salvo(2,salvoLocations16);
			Salvo salvo5_1_1 = new Salvo(1,salvoLocations17);
			Salvo salvo5_1_2 = new Salvo(1,salvoLocations18);
			Salvo salvo5_2_1 = new Salvo(2,salvoLocations19);
			Salvo salvo5_2_2 = new Salvo(2,salvoLocations20);
			Salvo salvo5_3_1 = new Salvo(3,salvoLocations21);
			Salvo salvo6_1_1 = new Salvo(1,salvoLocations21);
			Salvo salvo6_1_2 = new Salvo(1,salvoLocations21);
			Salvo salvo6_2_1 = new Salvo(2,salvoLocations21);
			Salvo salvo6_2_2 = new Salvo(2,salvoLocations21);
			Salvo salvo7_1_1 = new Salvo(1,salvoLocations21);
			Salvo salvo7_1_2 = new Salvo(1,salvoLocations21);
			Salvo salvo7_2_1 = new Salvo(2,salvoLocations21);
			Salvo salvo7_2_2 = new Salvo(2,salvoLocations21);
			Salvo salvo8_1_1 = new Salvo(1,salvoLocations21);
			Salvo salvo8_1_2 = new Salvo(1,salvoLocations21);
			Salvo salvo8_2_1 = new Salvo(2,salvoLocations21);
			Salvo salvo8_2_2 = new Salvo(2,salvoLocations21);

			LocalDateTime date1 = LocalDateTime.now().plusHours(0);
			LocalDateTime date2 = LocalDateTime.now().plusHours(1);
			LocalDateTime date3 = LocalDateTime.now().plusHours(2);
			LocalDateTime date4 = LocalDateTime.now().plusHours(3);
			LocalDateTime date5 = LocalDateTime.now().plusHours(4);
			LocalDateTime date6 = LocalDateTime.now().plusHours(5);
			LocalDateTime date7 = LocalDateTime.now().plusHours(5);
			LocalDateTime date8 = LocalDateTime.now().plusHours(6);

			Game game1 = new Game(date1);
			Game game2 = new Game(date2);
			Game game3 = new Game(date3);
			Game game4 = new Game(date4);
			Game game5 = new Game(date5);
			Game game6 = new Game(date6);
			Game game7 = new Game(date7);
			Game game8 = new Game(date8);

			Score score1_1 = new Score(game1, player1, 3, LocalDateTime.now().plusHours(2));
			Score score1_2 = new Score(game1, player2, 0, LocalDateTime.now().plusHours(2));
			Score score2_1 = new Score(game2, player1, 0, LocalDateTime.now().plusHours(4));
			Score score2_2 = new Score(game2, player2, 3, LocalDateTime.now().plusHours(4));
			Score score3_1 = new Score(game3, player2, 3, LocalDateTime.now().plusHours(6));
			Score score3_2 = new Score(game3, player3, 0, LocalDateTime.now().plusHours(6));
			Score score4_1 = new Score(game4, player1, 1, LocalDateTime.now().plusHours(8));
			Score score4_2 = new Score(game4, player2, 1, LocalDateTime.now().plusHours(8));
			Score score5_1 = new Score(game5, player3, 3, LocalDateTime.now().plusHours(9));
			Score score5_2 = new Score(game5, player1, 0, LocalDateTime.now().plusHours(9));
			Score score6_1 = new Score(game6, player3, 3, LocalDateTime.now().plusHours(9));
			Score score6_2 = new Score(game6, player1, 0, LocalDateTime.now().plusHours(9));
			Score score7_1 = new Score(game7, player3, 3, LocalDateTime.now().plusHours(9));
			Score score7_2 = new Score(game7, player4, 0, LocalDateTime.now().plusHours(9));
			Score score8_1 = new Score(game8, player4, 3, LocalDateTime.now().plusHours(9));



			GamePlayer gamePlayer1 = new GamePlayer(LocalDateTime.now(), game1, player1, Side.HERO);
			GamePlayer gamePlayer2 = new GamePlayer(LocalDateTime.now(), game1, player2, Side.VILLAIN);
			GamePlayer gamePlayer3 = new GamePlayer(LocalDateTime.now(), game2, player1, Side.VILLAIN);
			GamePlayer gamePlayer4 = new GamePlayer(LocalDateTime.now(), game2, player2, Side.HERO);
			GamePlayer gamePlayer5 = new GamePlayer(LocalDateTime.now(), game3, player2, Side.HERO);
			GamePlayer gamePlayer6 = new GamePlayer(LocalDateTime.now(), game3, player3, Side.HERO);
			GamePlayer gamePlayer7 = new GamePlayer(LocalDateTime.now(), game4, player1, Side.HERO);
			GamePlayer gamePlayer8 = new GamePlayer(LocalDateTime.now(), game4, player2, Side.HERO);
			GamePlayer gamePlayer9 = new GamePlayer(LocalDateTime.now(), game5, player3, Side.HERO);
			GamePlayer gamePlayer10 = new GamePlayer(LocalDateTime.now(), game5, player1, Side.HERO);
			GamePlayer gamePlayer11 = new GamePlayer(LocalDateTime.now(), game6, player4, Side.HERO);
			GamePlayer gamePlayer12 = new GamePlayer(LocalDateTime.now(), game6, player2, Side.HERO);
			GamePlayer gamePlayer13 = new GamePlayer(LocalDateTime.now(), game7, player3, Side.HERO);
			GamePlayer gamePlayer14 = new GamePlayer(LocalDateTime.now(), game7, player4, Side.HERO);

			GamePlayer gamePlayer16 = new GamePlayer(LocalDateTime.now(), game8, player4, Side.VILLAIN);



			// Guarda los datos en los respectivos repositorios
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);


			gamePlayer1.addShip(ship1_1_1);
			gamePlayer1.addShip(ship2_1_1);
			gamePlayer1.addShip(ship3_1_1);

			gamePlayer2.addShip(ship1_1_2);
			gamePlayer2.addShip(ship2_1_2);

			gamePlayer3.addShip(ship1_2_1);
			gamePlayer3.addShip(ship2_2_1);

			gamePlayer4.addShip(ship1_2_2);
			gamePlayer4.addShip(ship2_2_2);

			gamePlayer5.addShip(ship1_3_1);
			gamePlayer5.addShip(ship2_3_1);

			gamePlayer6.addShip(ship1_3_2);
			gamePlayer6.addShip(ship2_3_2);

			gamePlayer7.addShip(ship1_4_1);
			gamePlayer7.addShip(ship2_4_1);

			gamePlayer8.addShip(ship1_4_2);
			gamePlayer8.addShip(ship2_4_2);

			gamePlayer9.addShip(ship1_5_1);
			gamePlayer9.addShip(ship2_5_1);

			gamePlayer10.addShip(ship1_5_2);
			gamePlayer10.addShip(ship2_5_2);

			gamePlayer11.addShip(ship1_6_1);
			gamePlayer11.addShip(ship2_6_1);

			gamePlayer12.addShip(ship1_6_2);
			gamePlayer12.addShip(ship2_6_2);

			gamePlayer13.addShip(ship1_7_1);
			gamePlayer13.addShip(ship2_7_1);

			gamePlayer14.addShip(ship1_7_2);
			gamePlayer14.addShip(ship2_7_2);



			gamePlayer16.addShip(ship1_8_2);
			gamePlayer16.addShip(ship2_8_2);


			gamePlayer1.addSalvo(salvo1_1_1);
			gamePlayer2.addSalvo(salvo1_1_2);
			gamePlayer2.addSalvo(salvo1_2_2);
			gamePlayer1.addSalvo(salvo1_2_1);

			gamePlayer3.addSalvo(salvo2_1_1);
			gamePlayer4.addSalvo(salvo2_1_2);
			gamePlayer4.addSalvo(salvo2_2_2);
			gamePlayer3.addSalvo(salvo2_2_1);

			gamePlayer5.addSalvo(salvo3_1_1);
			gamePlayer6.addSalvo(salvo3_1_2);
			gamePlayer6.addSalvo(salvo3_2_2);
			gamePlayer5.addSalvo(salvo3_2_1);

			gamePlayer7.addSalvo(salvo4_1_1);
			gamePlayer8.addSalvo(salvo4_1_2);
			gamePlayer8.addSalvo(salvo4_2_2);
			gamePlayer7.addSalvo(salvo4_2_1);

			gamePlayer9.addSalvo(salvo5_1_1);
			gamePlayer10.addSalvo(salvo5_1_2);
			gamePlayer10.addSalvo(salvo5_2_2);
			gamePlayer9.addSalvo(salvo5_2_1);


			gamePlayer11.addSalvo(salvo6_1_1);
			gamePlayer12.addSalvo(salvo6_1_2);
			gamePlayer12.addSalvo(salvo6_2_2);
			gamePlayer11.addSalvo(salvo6_2_1);

			gamePlayer13.addSalvo(salvo7_1_1);
			gamePlayer14.addSalvo(salvo7_1_2);
			gamePlayer14.addSalvo(salvo7_2_2);
			gamePlayer13.addSalvo(salvo7_2_1);


			gamePlayer16.addSalvo(salvo8_1_2);
			gamePlayer16.addSalvo(salvo8_2_2);



			scoreRepository.save(score1_1);
			scoreRepository.save(score1_2);
			scoreRepository.save(score2_1);
			scoreRepository.save(score2_2);
			scoreRepository.save(score3_1);
			scoreRepository.save(score3_2);
			scoreRepository.save(score4_1);
			scoreRepository.save(score4_2);
			scoreRepository.save(score5_1);
			scoreRepository.save(score5_2);
			scoreRepository.save(score6_1);
			scoreRepository.save(score6_2);
			scoreRepository.save(score7_1);
			scoreRepository.save(score7_2);
			scoreRepository.save(score8_1);


			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);
			gamePlayerRepository.save(gamePlayer11);
			gamePlayerRepository.save(gamePlayer12);
			gamePlayerRepository.save(gamePlayer13);
			gamePlayerRepository.save(gamePlayer14);

			gamePlayerRepository.save(gamePlayer16);

		};
	}
	}

