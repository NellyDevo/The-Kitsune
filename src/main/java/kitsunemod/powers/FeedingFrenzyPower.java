package kitsunemod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import kitsunemod.KitsuneMod;

public class FeedingFrenzyPower extends AbstractKitsunePower implements NonStackablePower {

    public static final String POWER_ID = KitsuneMod.makeID("FeedingFrenzyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";


    public FeedingFrenzyPower(final AbstractCreature owner, int healAmount, int turns) {
        this.owner = owner;
        isTurnBased = true;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;

        amount = turns;
        amount2 = healAmount;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("brutality");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return 0;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new HealAction(owner, owner, amount2));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashPowerEffect(this)));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1];
    }
}
