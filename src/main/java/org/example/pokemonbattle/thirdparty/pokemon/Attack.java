package org.example.pokemonbattle.thirdparty.pokemon;

public class Attack {
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    private String name;
    private int damage;

    public String toString() {
        return "Attack{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", damage=" + damage +
                '}';
    }
}
