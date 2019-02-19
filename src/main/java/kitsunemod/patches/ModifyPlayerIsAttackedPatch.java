package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
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
public class ModifyPlayerIsAttackedPatch {
    public ModifyPlayerIsAttackedPatch() {
    }

    @SpireInsertPatch(
            localvars = {"damageAmount"},
            locator = ModifyPlayerIsAttackedPatch.LocatorPre.class
    )
    public static void InsertPre(AbstractCreature __instance, DamageInfo info, @ByRef int[] damageAmount) {
        int damage = KitsuneMod.receivePlayerIsAttacked(info, damageAmount[0]);
        if (damage < 0) {
            damage = 0;
        }
        damageAmount[0] = damage;
    }

    private static class LocatorPre extends SpireInsertLocator {
        private LocatorPre() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
