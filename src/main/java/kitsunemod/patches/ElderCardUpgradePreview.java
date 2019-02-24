package kitsunemod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import kitsunemod.cards.AbstractElderCard;

import java.lang.reflect.Field;

@SpirePatch(
        clz = SingleCardViewPopup.class,
        method = "render"
)
public class ElderCardUpgradePreview {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb) {
        try {
            Field reflectedCardField = SingleCardViewPopup.class.getDeclaredField("card");
            reflectedCardField.setAccessible(true);
            AbstractCard reflectedCard = (AbstractCard)reflectedCardField.get(__instance);
            if (reflectedCard instanceof AbstractElderCard && reflectedCard.timesUpgraded < 9) {
                ++reflectedCard.timesUpgraded;
                reflectedCardField.set(__instance, reflectedCard.makeCopy());
            }
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "displayUpgrades");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
