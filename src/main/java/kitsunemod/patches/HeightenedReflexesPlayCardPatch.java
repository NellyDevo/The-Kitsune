package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractCard.class,
        method = "hasEnoughEnergy"
)
public class HeightenedReflexesPlayCardPatch {
    public HeightenedReflexesPlayCardPatch() {
    }

    @SpireInsertPatch(
            locator = HeightenedReflexesPlayCardPatch.LocatorPre.class
    )
    public static SpireReturn<Boolean> InsertPre(AbstractCard __instance) {
        if (__instance.tags.contains(KitsuneTags.IGNORES_TURN_RESTRICTION)) {
            return SpireReturn.Return(true);
        }
        return SpireReturn.Continue();
    }

    private static class LocatorPre extends SpireInsertLocator {
        private LocatorPre() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "turnHasEnded");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
