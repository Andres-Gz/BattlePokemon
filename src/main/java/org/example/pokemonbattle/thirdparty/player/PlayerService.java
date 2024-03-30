package org.example.pokemonbattle.thirdparty.player;

import org.example.pokemonbattle.commons.helper.Constants;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Lazy
@Service
public class PlayerService {
    private final RestTemplate restTemplate;

    public PlayerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createPlayer(Player player) {
        String url = Constants.PLAYER_SERVICE_BASE_URL + Constants.CREATE_PLAYER_ENDPOINT;
        restTemplate.postForObject(url, player, Player.class);
    }
}
