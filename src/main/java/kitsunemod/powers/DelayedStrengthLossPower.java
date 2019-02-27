package kitsunemod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import kitsunemod.KitsuneMod;

public class DelayedStrengthLossPower extends TwoAmountPower {


    public static final String POWER_ID = KitsuneMod.makeID("DelayedStrengthLossPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public DelayedStrengthLossPower(final AbstractCreature owner, int turns, int amount) {
        this.owner = owner;
        isTurnBased = true;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.DEBUFF;
        this.amount = turns;
        this.amount2 = amount;


        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("flex");
        updateDescription();
    }

    @Override
    public void onRemove() {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, StrengthPower.POWER_ID, amount2));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        this.stackPower(-1);
        updateDescription();
        if (amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
    }
}
