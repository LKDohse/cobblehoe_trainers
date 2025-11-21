package net.electricbudgie.battle;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI;
import com.gitlab.srcmc.rctapi.api.battle.BattleFormat;
import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import com.gitlab.srcmc.rctapi.api.models.BagItemModel;
import com.gitlab.srcmc.rctapi.api.models.PokemonModel;
import com.gitlab.srcmc.rctapi.api.models.TrainerModel;
import com.gitlab.srcmc.rctapi.api.util.JTO;

import java.util.List;
import java.util.UUID;

public class TrainerBattleData extends TrainerModel {
    private String identity;
    private BattleFormat battleFormat;
    private BattleRules battleRules;
    public TrainerBattleData(){
        this(null, "Trainer", BattleFormat.GEN_9_SINGLES, new BattleRules(), JTO.of(RCTBattleAI::new), List.of(), List.of());
    }

    public TrainerBattleData(String identity, String name, BattleFormat battleFormat, BattleRules battleRules, JTO<BattleAI> ai, List<BagItemModel> bag, List<PokemonModel> team) {
        super(name, ai, bag, team);
        this.identity = identity;
        this.battleFormat = battleFormat;
        this.battleRules = battleRules;
    }

    public String getIdentity() {
        if(this.identity == null) {
            this.identity = this.getName().getLiteral() != null
                    ? this.getName().getLiteral()
                    : (this.getName().getTranslatable() != null
                    ? this.getName().getTranslatable()
                    : UUID.randomUUID().toString());
        }

        return this.identity;
    }

    public BattleFormat getBattleFormat() {
        return this.battleFormat;
    }

    public BattleRules getBattleRules() {
        return this.battleRules;
    }
}
