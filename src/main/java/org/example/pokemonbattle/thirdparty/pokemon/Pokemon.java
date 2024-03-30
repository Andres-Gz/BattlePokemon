package org.example.pokemonbattle.thirdparty.pokemon;

import java.util.List;

public class Pokemon {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    private int health;
    private List<Attack> attacks;

    public void reduceHealth(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0; // Asegurar que la salud no sea negativa
        }
    }


}
