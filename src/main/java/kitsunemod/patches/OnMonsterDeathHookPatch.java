package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import kitsunemod.KitsuneMod;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = {boolean.class}
)
public class OnMonsterDeathHookPatch {
    public OnMonsterDeathHookPatch() {
    }

    @SpireInsertPatch(
            locator = OnMonsterDeathHookPatch.LocatorPre.class
    )
    public static void InsertPre(AbstractMonster __instance, boolean __unused) {
        KitsuneMod.receiveOnMonsterDeath(__instance);
    }

    private static class LocatorPre extends SpireInsertLocator {
        private LocatorPre() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
