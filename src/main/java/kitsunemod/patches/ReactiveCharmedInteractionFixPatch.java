package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.ReactivePower;
import kitsunemod.powers.CharmMonsterPower;

@SpirePatch(
        clz = ReactivePower.class,
        method = "onAttacked"
)
public class ReactiveCharmedInteractionFixPatch {

    public static SpireReturn<Integer> Prefix(ReactivePower __instance, DamageInfo info, int damageAmount) {
        if (__instance.owner.hasPower(CharmMonsterPower.POWER_ID)) {
            return SpireReturn.Return(damageAmount);
        }
        return SpireReturn.Continue();
    }
}
