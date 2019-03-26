package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;
import kitsunemod.wisps.WillOWisp;

public class FaeProtectionPower extends AbstractKitsunePower implements OnLoseBlockPower {
    public static final String POWER_ID = KitsuneMod.makeID("FaeProtectionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FaeProtectionPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        this.amount = amount;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/LightPower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/LightPower_32.png"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        WillOWisp.resetFormation();
    }

    @Override
    public int onLoseBlock(DamageInfo info, int lostBlock) {
        flash();
        KitsuneMod.wisps.subList(0, amount).clear();
        KitsuneMod.calculateWispPositions();
        return lostBlock;
    }

    @Override
    public boolean shouldHoldFire() {
        return true;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + (amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }
}
