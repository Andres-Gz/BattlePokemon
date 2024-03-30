
package org.example.pokemonbattle;

import jakarta.annotation.PostConstruct;
import org.example.pokemonbattle.commons.helper.Constants;
import org.example.pokemonbattle.thirdparty.player.Player;
import org.example.pokemonbattle.thirdparty.player.PlayerService;
import org.example.pokemonbattle.thirdparty.pokemon.Pokemon;
import org.example.pokemonbattle.thirdparty.pokemon.PokemonService;
import org.example.pokemonbattle.thirdparty.pokemon.Attack;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@ComponentScan(basePackages = {"org.example.pokemonbattle.thirdparty.player", "org.example.pokemonbattle.thirdparty.pokemon"})
@SpringBootApplication
public class PokemonBattleServiceApplication {
    private final Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(PokemonBattleServiceApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    private final PokemonService pokemonService;
    private final PlayerService playerService;

    public PokemonBattleServiceApplication(@Lazy PokemonService pokemonService, @Lazy PlayerService playerService) {
        this.pokemonService = pokemonService;
        this.playerService = playerService;
    }

    @PostConstruct
    public void startGame() {

        try {
            System.out.println("Bienvenido al juego Pokémon Battle!");

            List<Pokemon> pokemons = pokemonService.getAvailablePokemons();
            showAvailablePokemons(pokemons);

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Ingrese su nombre: ");
                String playerName = scanner.nextLine();

                Set<Long> selectedPokemonIds = new HashSet<>();
                while (selectedPokemonIds.size() < Constants.MAX_POKEMONS) {
                    System.out.print("Seleccione un Pokémon por nombre: ");
                    String pokemonName = scanner.nextLine();
                    Optional<Pokemon> selectedPokemon = pokemons.stream()
                            .filter(pokemon -> pokemon.getName().equalsIgnoreCase(pokemonName))
                            .findFirst();
                    if (selectedPokemon.isPresent()) {
                        selectedPokemonIds.add((long) selectedPokemon.get().getId());
                    } else {
                        System.out.println("No se encontró el Pokémon. Intenta de nuevo.");
                    }
                }

                Player player = new Player(playerName);
                player.setPokemonIds(selectedPokemonIds);
                System.out.println("Hello");
                playerService.createPlayer(player);

                // Crear oponente con Pokémon aleatorios
                System.out.println("2");
                Set<Long> enemyPokemonIds = new HashSet<>();
                while (enemyPokemonIds.size() < Constants.MAX_POKEMONS) {
                    int randomIndex = random.nextInt(pokemons.size());
                    enemyPokemonIds.add((long) pokemons.get(randomIndex).getId());
                }

                System.out.println("3");

                Player enemyPlayer = new Player("Oponente");
                enemyPlayer.setPokemonIds(enemyPokemonIds);
                playerService.createPlayer(enemyPlayer);

                // Comenzar la batalla
                System.out.println("¡Comienza la batalla!");
                startBattle(player, enemyPlayer);
            }
        } catch (Exception e) {
            System.err.println("Error al iniciar el juego: " + e.getMessage());
            e.printStackTrace();
            // Puedes lanzar una excepción personalizada o manejar el error de otra manera
        }
    }

    private void startBattle(Player player, Player enemyPlayer) {
        List<Pokemon> playerPokemonTeam = getPlayerPokemonTeam(player);
        List<Pokemon> enemyPokemonTeam = getPlayerPokemonTeam(enemyPlayer);

        while (areTeamsAlive(playerPokemonTeam, enemyPokemonTeam)) {
            System.out.println("\nTurno del Jugador:");
            performTurn(playerPokemonTeam, enemyPokemonTeam);

            if (!areTeamsAlive(playerPokemonTeam, enemyPokemonTeam)) {
                break;
            }

            System.out.println("\nTurno del Oponente:");
            performTurn(enemyPokemonTeam, playerPokemonTeam);
        }

        Player winner = playerPokemonTeam.isEmpty() ? enemyPlayer : player;
        System.out.println("¡El equipo de " + winner.getName() + " ha ganado la batalla!");
    }

    private List<Pokemon> getPlayerPokemonTeam(Player player) {
        List<Pokemon> pokemons = new ArrayList<>();
        for (long pokemonId : player.getPokemonIds()) {
            Pokemon pokemon = pokemonService.getPokemonById(pokemonId);
            if (pokemon != null) {
                pokemons.add(pokemon);
            }
        }
        return pokemons;
    }

    private boolean areTeamsAlive(List<Pokemon> team1, List<Pokemon> team2) {
        return !team1.isEmpty() && !team2.isEmpty();
    }

    private void performTurn(List<Pokemon> attackingTeam, List<Pokemon> targetTeam) {
        for (Pokemon attacker : attackingTeam) {
            if (targetTeam.isEmpty()) {
                break;
            }

            Pokemon target = targetTeam.get(0); // En este ejemplo, solo se ataca al primer Pokémon del equipo oponente
            Attack selectedAttack = attacker.getAttacks().get(random.nextInt(attacker.getAttacks().size()));
            int damage = selectedAttack.getDamage();
            target.reduceHealth(damage);

            System.out.println(attacker.getName() + " usa " + selectedAttack.getName() +
                    " y hace " + damage + " de daño a " + target.getName());

            if (target.getHealth() <= 0) {
                targetTeam.remove(target);
                System.out.println(target.getName() + " ha sido derrotado!");
            }
        }
    }

    private void showAvailablePokemons(List<Pokemon> pokemons) {
        pokemons.forEach(pokemon -> {
            System.out.println(pokemon.getName() + " tiene los siguientes ataques:");
            pokemon.getAttacks().stream()
                    .map(Attack::getName)
                    .forEach(attackName -> System.out.println("- " + attackName));
            System.out.println();
        });
    }
}
