package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import kitsunemod.cards.attacks.AncientMalice;

@SpirePatch(
        clz = AbstractCard.class,
        method = "calculateCardDamage"
)
public class AncientMaliceExtraVulnerableIsAdditivePatch {
    public static float storageUnit = 0.0f;

    @SpireInsertPatch(
            locator = LocatorPre.class,
            localvars = {"tmp", "p"}
    )
    public static void InsertPre(AbstractCard __instance, AbstractMonster mo, float tmp, AbstractPower p) {
        if (__instance instanceof AncientMalice && p instanceof VulnerablePower) {
            storageUnit = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL) - tmp;
        }
    }

    @SpireInsertPatch(
            locator = LocatorPost.class,
            localvars = {"tmp"}
    )
    public static void InsertPost(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
        if (storageUnit != 0.0f) {
            tmp[0] += storageUnit;
        }
        storageUnit = 0.0f;
    }

    public static class LocatorPre extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "atDamageReceive");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    public static class LocatorPost extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "damage");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
