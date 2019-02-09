package kitsunemod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.powers.*;

import kitsunemod.KitsuneMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoulstealPower extends AbstractPower {
    public AbstractCreature source;

    public static final String POWER_ID = KitsuneMod.makeID("SoulstealPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    private static final int LIGHT_DARK_PER_STACK = 2;
    private static final int WILL_O_WISPS_TOTAL = 1;


    public SoulstealPower(final AbstractCreature owner, final AbstractCreature source, final int stacks) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        isTurnBased = false;
        amount = stacks;
        updateDescription();
        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("constricted");
        this.source = source;

    }

    @Override
    public void onAttack(DamageInfo info, int amount, AbstractCreature target) {
        if (target instanceof AbstractPlayer) {
            //TODO: Implement this. Need to wire up light and dark as dummy powers at least
            AbstractPlayer targetPlayer = (AbstractPlayer)target;
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

    }


    @Override
    public void updateDescription() {

        if (amount == 1) {
            description = DESCRIPTIONS[0] + (amount * LIGHT_DARK_PER_STACK) + DESCRIPTIONS[1] + WILL_O_WISPS_TOTAL + DESCRIPTIONS[2] + DESCRIPTIONS[4];
        }

        else if (amount > 1) {
            description = DESCRIPTIONS[0] + (amount * LIGHT_DARK_PER_STACK) + DESCRIPTIONS[1] + WILL_O_WISPS_TOTAL + DESCRIPTIONS[3] + DESCRIPTIONS[4];
        }
    }

}
