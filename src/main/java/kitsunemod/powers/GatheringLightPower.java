package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyLightAction;

public class GatheringLightPower extends AbstractKitsunePower {

    public static final String POWER_ID = KitsuneMod.makeID("GatheringLightPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public GatheringLightPower(AbstractCreature owner, int stacks) {
        this.owner = owner;
        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        amount = stacks;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/LightPower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/LightPower_32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public boolean shouldConsume(AbstractPower p) {
        return !p.ID.equals(LightPower.POWER_ID);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
