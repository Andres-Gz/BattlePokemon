package org.example.pokemonbattle.thirdparty.pokemon;

import org.example.pokemonbattle.commons.helper.Constants;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PokemonService {

    private RestTemplate restTemplate = new RestTemplate();

    public List<Pokemon> getAvailablePokemons() {
        String url = Constants.POKEMON_SERVICE_BASE_URL + Constants.AVAILABLE_POKEMONS_ENDPOINT;
        Pokemon[] pokemons = restTemplate.getForObject(url, Pokemon[].class);
        return pokemons != null ? Arrays.asList(pokemons) : List.of();
    }

    public Pokemon getPokemonById(long id) {
        String url = Constants.POKEMON_SERVICE_BASE_URL + Constants.POKEMON_BY_ID_ENDPOINT + id;
        return restTemplate.getForObject(url, Pokemon.class);
    }
}
