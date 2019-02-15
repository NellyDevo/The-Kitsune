package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;

public class WornPearl extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("WornPearl");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    public WornPearl() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }


    //so
    //starter relics never get onEquip called
    //for [curse word]ing no reason
    //this is a workaround while preserving /some/ readability, change the base values in the respective shapes please
    public static final int FOX_BONUS_STR = FoxShapePower.BONUS_STRENGTH;
    public static final int FOX_BONUS_DEX = FoxShapePower.BONUS_DEXTERITY;
    public static final int KITSUNE_BONUS_STR = KitsuneShapePower.BONUS_STRENGTH;
    public static final int KITSUNE_BONUS_DEX = KitsuneShapePower.BONUS_DEXTERITY;
    public static final int HUMAN_BONUS_STR = HumanShapePower.BONUS_STRENGTH;
    public static final int HUMAN_BONUS_DEX = HumanShapePower.BONUS_DEXTERITY;

    @Override
    public void onUnequip() {
        FoxShapePower.BONUS_STRENGTH -= FOX_BONUS_STR;
        FoxShapePower.BONUS_DEXTERITY -= FOX_BONUS_DEX;

        KitsuneShapePower.BONUS_STRENGTH -= KITSUNE_BONUS_STR;
        KitsuneShapePower.BONUS_DEXTERITY -= KITSUNE_BONUS_DEX;

        HumanShapePower.BONUS_STRENGTH -= HUMAN_BONUS_STR;
        HumanShapePower.BONUS_DEXTERITY -= HUMAN_BONUS_DEX;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onChangeShape(KitsuneMod.KitsuneShapes shape) {
        description = DESCRIPTIONS[0];

        //TODO handle ninetailed form; for that matter decide what we want to do with ninetailed form re: str/dex now
        if (shape == KitsuneMod.KitsuneShapes.FOX) {
            description += DESCRIPTIONS[1] + FOX_BONUS_STR + DESCRIPTIONS[4] + FOX_BONUS_DEX + DESCRIPTIONS[5];
        }
        if (shape == KitsuneMod.KitsuneShapes.KITSUNE) {
            description += DESCRIPTIONS[2] + KITSUNE_BONUS_STR + DESCRIPTIONS[4] + KITSUNE_BONUS_DEX + DESCRIPTIONS[5];
        }
        if (shape == KitsuneMod.KitsuneShapes.HUMAN) {
            description += DESCRIPTIONS[3] + FOX_BONUS_STR + DESCRIPTIONS[4] + FOX_BONUS_DEX + DESCRIPTIONS[5];
        }

        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.initializeTips();
    }

    @Override
    public void onVictory() {
        description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(name,description));
        this.initializeTips();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WornPearl();
    }
}
