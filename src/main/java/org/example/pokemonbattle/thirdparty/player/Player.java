package org.example.pokemonbattle.thirdparty.player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Set;

public class Player {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> pokemonIds = new HashSet<>();

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Set<Long> getPokemonIds() {
        return pokemonIds;
    }

    public void setPokemonIds(Set<Long> pokemonIds) {
        this.pokemonIds = pokemonIds;
    }

    public void addPokemonId(Long pokemonId) {
        this.pokemonIds.add(pokemonId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
