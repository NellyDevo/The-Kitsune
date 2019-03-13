package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyLightAction;

public class GatheringLightPower extends TwoAmountPower implements GatheringPower {

    public static final String POWER_ID = KitsuneMod.makeID("GatheringLightPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    private int thornsGained = 0;

    public GatheringLightPower(AbstractCreature owner, int lightAmount, int thornsAmount) {
        this.owner = owner;
        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        amount = lightAmount;
        amount2 = thornsAmount;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/LightPower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/LightPower_32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onApplyLightOrDark(boolean isLight) {
        if (isLight) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new ThornsPower(owner, amount2), amount2));
            thornsGained += amount2;
        }
    }

    @Override
    public void atStartOfTurn() {
        if (thornsGained > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new ThornsPower(owner, -thornsGained), -thornsGained));
            thornsGained = 0;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
    }
}
