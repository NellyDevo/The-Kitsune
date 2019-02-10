package kitsunemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import kitsunemod.KitsuneMod;

public class KitsuneShapePower extends AbstractPower {

    public AbstractCreature source;
    public static final int BONUS_DEXTERITY = 1;
    public static final int BONUS_STRENGTH = 1;

    public static final String POWER_ID = KitsuneMod.makeID("KitsuneShapePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";


    public KitsuneShapePower(final AbstractCreature owner, final AbstractCreature source) {
        this.owner = owner;
        this.source = source;

        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        canGoNegative = false;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("demonForm");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new StrengthPower(owner, BONUS_STRENGTH), BONUS_STRENGTH));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new DexterityPower(owner, BONUS_DEXTERITY), BONUS_DEXTERITY));
    }
    @Override
    public void onRemove() {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, source, StrengthPower.POWER_ID, BONUS_STRENGTH));
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, source, DexterityPower.POWER_ID, BONUS_DEXTERITY));
    }


    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
