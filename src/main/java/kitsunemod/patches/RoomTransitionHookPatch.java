package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import kitsunemod.KitsuneMod;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.nextRoom;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "nextRoomTransition",
        paramtypez = {SaveFile.class}
)
public class RoomTransitionHookPatch {
    public RoomTransitionHookPatch() {
    }

    @SpireInsertPatch(
            locator = RoomTransitionHookPatch.LocatorPost.class
    )
    public static void InsertPost(AbstractDungeon __instance, SaveFile saveFile) {
        if (AbstractDungeon.nextRoom != null && AbstractDungeon.nextRoom.room != null) {
            KitsuneMod.receiveRoomEntered(nextRoom.room);
        }
    }

    private static class LocatorPost extends SpireInsertLocator {
        private LocatorPost() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "miscRng");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
