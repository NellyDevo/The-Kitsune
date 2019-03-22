package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import kitsunemod.KitsuneMod;

@SpirePatch(
        clz = GameActionManager.class,
        method = "callEndOfTurnActions"
)
public class TriggerOnPlayerEndTurnPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(GameActionManager __instance) {
        KitsuneMod.onTriggerEndOfPlayerTurnActions();
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
