package kitsunemod.character;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.cards.basic.DancingLights;
import kitsunemod.cards.basic.Defend;
import kitsunemod.cards.basic.Strike;
import kitsunemod.cards.basic.Wink;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneEnum;
import kitsunemod.relics.StarterRelic;

import java.util.ArrayList;

public class KitsuneCharacter extends CustomPlayer {
    public static final int ENERGY_PER_TURN = 3;
    public static final String MY_CHARACTER_SHOULDER_2 = "kitsunemod/images/char/shoulder2.png";
    public static final String MY_CHARACTER_SHOULDER_1 = "kitsunemod/images/char/shoulder.png";
    public static final String MY_CHARACTER_CORPSE = "kitsunemod/images/char/corpse.png";
    public static final String MY_CHARACTER_ANIMATION = "kitsunemod/images/char/idle/Animation.scml";
    private static final String ID = KitsuneMod.makeID("KitsuneCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;
    private static final float DIALOG_X_ADJUSTMENT = 0.0F;
    private static final float DIALOG_Y_ADJUSTMENT = 220.0F;
    public static final String[] orbTextures = {
            "kitsunemod/images/char/orb/layer1.png",
            "kitsunemod/images/char/orb/layer2.png",
            "kitsunemod/images/char/orb/layer3.png",
            "kitsunemod/images/char/orb/layer4.png",
            "kitsunemod/images/char/orb/layer5.png",
            "kitsunemod/images/char/orb/layer6.png",
            "kitsunemod/images/char/orb/layer1d.png",
            "kitsunemod/images/char/orb/layer2d.png",
            "kitsunemod/images/char/orb/layer3d.png",
            "kitsunemod/images/char/orb/layer4d.png",
            "kitsunemod/images/char/orb/layer5d.png"
    };

    public KitsuneCharacter(String name) {
        super(name, KitsuneEnum.KITSUNE_CLASS, orbTextures, "kitsunemod/images/char/orb/vfx.png", null, new SpriterAnimation(MY_CHARACTER_ANIMATION));

        dialogX = drawX + DIALOG_X_ADJUSTMENT * Settings.scale;
        dialogY = drawY + DIALOG_Y_ADJUSTMENT * Settings.scale;

        initializeClass(null,
                MY_CHARACTER_SHOULDER_2,
                MY_CHARACTER_SHOULDER_1,
                MY_CHARACTER_CORPSE,
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1]; //UPDATE BODY TEXT :(
    }

    @Override
    public Color getSlashAttackColor() {
        return KitsuneMod.kitsuneColor;
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[0];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.KITSUNE_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return KitsuneMod.kitsuneColor;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
                };
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        //added because preBattlePrep in AbstractPlayer does similar things, and until we promote the shape tracking logic here we need to do this at least
        AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(this, this, KitsuneMod.KitsuneShapes.KITSUNE));
    }

    @Override
    public Color getCardTrailColor() {
        return KitsuneMod.kitsuneColor;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.sound.playA("ATTACK_FAST", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new KitsuneCharacter(name);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Wink.ID);
        retVal.add(DancingLights.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(StarterRelic.ID);
        UnlockTracker.markRelicAsSeen(StarterRelic.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                77, 77, 0, 99, 5, //starting hp, max hp, max orbs, starting gold, starting hand size
                this, getStartingRelics(), getStartingDeck(), false);
    }
}
