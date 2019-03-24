package kitsunemod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import kitsunemod.KitsuneMod;
import kitsunemod.wisps.WillOWisp;

public class WillOWispPatch {

    @SpirePatch(
            clz = GameActionManager.class,
            method = "callEndOfTurnActions"
    )
    public static class triggerOnPlayerEndTurnPatch {
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

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "resetPlayer"
    )
    public static class resetPlayerPatch {

        public static void Prefix() {
            KitsuneMod.wisps.clear();
        }

    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class preBattlePrepPatch {

        public static void Prefix(AbstractPlayer __instance) {
            KitsuneMod.wisps.clear();
        }

    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "render"
    )
    public static class postPlayerRenderPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractRoom __instance, SpriteBatch sb) {
            if (!KitsuneMod.wisps.isEmpty() && AbstractDungeon.player != null) {
                for (WillOWisp wisp : KitsuneMod.wisps) {
                    if (!wisp.renderBehind) { //other wisps are rendered in BaseMod PreRoomRenderSubscriber
                        wisp.render(sb);
                    }
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
