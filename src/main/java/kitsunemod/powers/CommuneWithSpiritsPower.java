package kitsunemod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;

public class CommuneWithSpiritsPower extends TwoAmountPower implements WispAffectingPower {
    public static final String POWER_ID = KitsuneMod.makeID("CommuneWithSpirits");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CommuneWithSpiritsPower(AbstractCreature owner, int extraWisps, int extraCards) {
        this.owner = owner;
        amount = extraWisps;
        amount2 = extraCards;
        name = NAME;
        ID = POWER_ID;
        loadRegion("echo");
        updateDescription();
    }

    @Override
    public int onChannelWisp(int amount) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, amount2));
        amount += this.amount;
        return amount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount > 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[1]) + DESCRIPTIONS[3] + (amount2 > 1 ? DESCRIPTIONS[5] : DESCRIPTIONS[4]);
    }

    @Override
    public void stackPower(int amount) {
        this.amount += amount;
        ++amount2;
        updateDescription();
    }
}
