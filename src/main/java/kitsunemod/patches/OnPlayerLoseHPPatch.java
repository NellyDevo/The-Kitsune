package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;
import kitsunemod.KitsuneMod;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage",
        paramtypez = {DamageInfo.class}
)
public class OnPlayerLoseHPPatch {
    public OnPlayerLoseHPPatch() {
    }

    @SpireInsertPatch(
            localvars = {"damageAmount"},
            locator = OnPlayerLoseHPPatch.LocatorPre.class
    )
    public static void InsertPre(AbstractCreature __instance, DamageInfo info, @ByRef int[] damageAmount) {
        KitsuneMod.receivePlayerTookDamage(info, damageAmount[0]);
    }

    private static class LocatorPre extends SpireInsertLocator {
        private LocatorPre() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "damageReceivedThisTurn");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
