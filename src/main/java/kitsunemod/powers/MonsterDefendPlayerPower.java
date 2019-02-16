package kitsunemod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;

public class MonsterDefendPlayerPower extends AbstractPower implements NonStackablePower {

    public static final String POWER_ID = KitsuneMod.makeID("MonsterDefendPlayer");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";
    private AbstractCreature taker;

    public MonsterDefendPlayerPower(final AbstractCreature owner, AbstractCreature taker, int amount) {

        this.owner = owner;
        this.taker = taker;
        this.amount = amount;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("afterImage");
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!taker.isDeadOrEscaped()) {
            if (amount < damageAmount) {
                damageAmount -= amount;
                AbstractDungeon.actionManager.addToTop(new DamageAction(taker, new DamageInfo(info.owner, amount)));
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner,this));
            } else {
                AbstractDungeon.actionManager.addToTop(new DamageAction(taker, new DamageInfo(info.owner, damageAmount)));
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(owner, owner, this, damageAmount));
                damageAmount = 0;
            }
        }
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner,this));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
