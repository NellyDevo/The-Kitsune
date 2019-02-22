package kitsunemod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import kitsunemod.KitsuneMod;

public class LightPower extends AbstractPower {

    public AbstractCreature source;
    public static final int TRIGGER_BASE_STACKS = 9;
    public static final float EFFECT_MULTIPLIER = 2f;

    public static final String POWER_ID = KitsuneMod.makeID("LightPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    private int baseThreshold;

    public LightPower(final AbstractCreature owner, final AbstractCreature source, final int stacks) {

        this.owner = owner;
        this.source = source;

        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        amount = stacks;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("echo");

        baseThreshold = TRIGGER_BASE_STACKS;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {

        if (amount >= calculateThreshold(baseThreshold)) {
            AbstractDungeon.effectList.add(new FlashPowerEffect(this));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, (int)(amount * EFFECT_MULTIPLIER)));
            KitsuneMod.receiveOnTriggerLight();
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            boolean shouldConsume = true;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof GatheringPower) {
                    if (!((GatheringPower)p).shouldConsume(this)) {
                        shouldConsume = false;
                    }
                }
            }
            if (shouldConsume) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
            }
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

    }

    private int calculateThreshold(int amount) {
        if (owner.hasPower(MasteryOfLightAndDarkPower.POWER_ID)) {
            amount = ((MasteryOfLightAndDarkPower)owner.getPower(MasteryOfLightAndDarkPower.POWER_ID)).affectThreshold(amount);
        }
        return amount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + EFFECT_MULTIPLIER + DESCRIPTIONS[1] + calculateThreshold(baseThreshold) + DESCRIPTIONS[2];
    }
}
