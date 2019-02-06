package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import kitsunemod.cards.AbstractElderCard;

@SpirePatch(
        clz = CardLibrary.class,
        method = "getCopy"
)
public class LoadElderCardPatch {
    public static AbstractCard Postfix(AbstractCard __result, String key, final int upgradeTime, final int misc) {
        if (__result instanceof AbstractElderCard) {
            __result.timesUpgraded = upgradeTime;
            __result = __result.makeStatEquivalentCopy();
        }
        return __result;
    }
}
