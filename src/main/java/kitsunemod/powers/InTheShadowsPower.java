package kitsunemod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;

public class InTheShadowsPower extends AbstractPower {

    private int healAmount;

    public static final String POWER_ID = KitsuneMod.makeID("InTheShadowsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";


    public InTheShadowsPower(
            final AbstractCreature owner,
            final int stacks,
            final int healAmount) {

        this.owner = owner;
        this.healAmount = healAmount;

        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        amount = stacks;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("wraithForm");
        updateDescription();

    }

    @Override
    public int onLoseHp(int damageAmount) {

        if (damageAmount > 0) {
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            AbstractDungeon.actionManager.addToBottom(new HealAction(owner, owner, healAmount));
        }

        return 0;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }


    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + healAmount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];

        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + healAmount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[3];
        }
    }
}
