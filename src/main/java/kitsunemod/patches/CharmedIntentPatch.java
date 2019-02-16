package kitsunemod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import kitsunemod.powers.CharmMonsterPower;

import java.lang.reflect.Field;
import java.util.ArrayList;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "renderIntentVfxBehind"
)
@SpirePatch(
        clz = AbstractMonster.class,
        method = "renderIntentVfxAfter"
)
public class CharmedIntentPatch {
    private static TextureAtlas.AtlasRegion newImg;

    public static void Prefix(AbstractMonster __instance, SpriteBatch sb) {
        if (__instance.hasPower(CharmMonsterPower.POWER_ID)) {
            try {
                if (newImg == null) {
                    newImg = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/vfx/tiny_heart.png"),0, 0, 35, 35);
                }
                Field intentVfxField = AbstractMonster.class.getDeclaredField("intentVfx");
                intentVfxField.setAccessible(true);
                ArrayList<AbstractGameEffect> reflectedList = (ArrayList<AbstractGameEffect>)intentVfxField.get(__instance);
                for (AbstractGameEffect e : reflectedList) {
                    if (e instanceof StunStarEffect) {
                        if (!TamperedWithField.tamperedWith.get(e)) {
                            Field imgField = StunStarEffect.class.getDeclaredField("img");
                            imgField.setAccessible(true);
                            imgField.set(e, newImg);
                            Field colorField = AbstractGameEffect.class.getDeclaredField("color");
                            colorField.setAccessible(true);
                            Color newColor = Color.WHITE.cpy();
                            newColor.a = ((Color)colorField.get(e)).a;
                            colorField.set(e, newColor);
                            Field rotationField = AbstractGameEffect.class.getDeclaredField("rotation");
                            rotationField.setAccessible(true);
                            rotationField.set(e, 60.0f);
                            TamperedWithField.tamperedWith.set(e, true);
                        }
                    }
                }
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SpirePatch(
            clz = StunStarEffect.class,
            method = SpirePatch.CLASS
    )
    public static class TamperedWithField {
        public static SpireField<Boolean> tamperedWith = new SpireField<>(() -> false);
    }
}
