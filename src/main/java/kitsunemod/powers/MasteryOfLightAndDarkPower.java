package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ApplyLightAction;

public class MasteryOfLightAndDarkPower extends AbstractPower {
    public static final String POWER_ID = KitsuneMod.makeID("MasteryOfLightAndDarkPower");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MasteryOfLightAndDarkPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        name = NAME;
        ID = POWER_ID;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/MasteryOfLightAndDarkPower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/MasteryOfLightAndDarkPower_32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    public void onTriggerLight() {
        AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(owner, owner, amount));
    }

    public void onTriggerDark() {
        AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(owner, owner, amount));
    }

    int affectThreshold(int amount) {
        return amount + this.amount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }
}
