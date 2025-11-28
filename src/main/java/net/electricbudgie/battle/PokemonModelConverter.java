package net.electricbudgie.battle;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.gitlab.srcmc.rctapi.api.models.PokemonModel;
import net.minecraft.registry.Registries;

import java.util.stream.Collectors;

public class PokemonModelConverter {

    public static PokemonModel getModel(Pokemon pokemon) {
        PokemonModel pokemonModel = new PokemonModel(
                pokemon.getSpecies().getName().toLowerCase(),
                pokemon.getGender().asString(),
                pokemon.getLevel(),
                pokemon.getNature().getName().toString().toLowerCase(),
                pokemon.getAbility().getName().toLowerCase(),
                pokemon.getMoveSet().getMoves().stream().map(Move::getName).collect(Collectors.toSet()),
                getIvStats(pokemon),
                getEvStats(pokemon),
                pokemon.getShiny(),
                getHeldItem(pokemon),
                pokemon.getAspects()
        );
        return pokemonModel;
    }

    private static String getHeldItem(Pokemon pokemon){
        if (pokemon.heldItem().isEmpty()) return "everstone";
        return pokemon.heldItem().getItem().toString();
    }

    private static PokemonModel.StatsModel getIvStats(Pokemon pokemon) {
        var statsModel = new PokemonModel.StatsModel(
                pokemon.getIvs().getOrDefault(Stats.HP),
                pokemon.getIvs().getOrDefault(Stats.ATTACK),
                pokemon.getIvs().getOrDefault(Stats.DEFENCE),
                pokemon.getIvs().getOrDefault(Stats.SPECIAL_ATTACK),
                pokemon.getIvs().getOrDefault(Stats.SPECIAL_DEFENCE),
                pokemon.getIvs().getOrDefault(Stats.SPEED));

        return statsModel;
    }

    private static PokemonModel.StatsModel getEvStats(Pokemon pokemon) {
        var statsModel = new PokemonModel.StatsModel(
                pokemon.getEvs().getOrDefault(Stats.HP),
                pokemon.getEvs().getOrDefault(Stats.ATTACK),
                pokemon.getEvs().getOrDefault(Stats.DEFENCE),
                pokemon.getEvs().getOrDefault(Stats.SPECIAL_ATTACK),
                pokemon.getEvs().getOrDefault(Stats.SPECIAL_DEFENCE),
                pokemon.getEvs().getOrDefault(Stats.SPEED));

        return statsModel;
    }

}

