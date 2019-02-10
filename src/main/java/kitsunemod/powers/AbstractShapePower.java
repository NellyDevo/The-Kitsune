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

public abstract class AbstractShapePower extends AbstractPower {

    public AbstractCreature source;

    private int bonusDexterity;
    private int bonusStrength;
    private KitsuneMod.KitsuneShapes shape;


    public AbstractShapePower(
            final AbstractCreature owner,
            final AbstractCreature source,
            final KitsuneMod.KitsuneShapes shape,
            final int bonusStrength,
            final int bonusDexterity) {
        this.owner = owner;
        this.source = source;
        this.bonusDexterity = bonusDexterity;
        this.bonusStrength = bonusStrength;
        this.shape = shape;

        isTurnBased = false;
        canGoNegative = false;
        type = PowerType.BUFF;

    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new StrengthPower(owner, bonusStrength), bonusStrength));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new DexterityPower(owner, bonusDexterity), bonusDexterity));
    }
    @Override
    public void onRemove() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new StrengthPower(owner, -bonusStrength), -bonusStrength));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new DexterityPower(owner, -bonusDexterity), -bonusDexterity));
    }

    public KitsuneMod.KitsuneShapes getShape() {
        return shape;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
