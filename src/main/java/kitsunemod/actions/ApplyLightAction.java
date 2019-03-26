package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;

public class ApplyLightAction extends AbstractGameAction {

    public ApplyLightAction(
            final AbstractCreature target,
            final AbstractCreature source,
            final int stacks)

    {
        this.source = source;
        this.target = target;
        this.amount = stacks;
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            KitsuneMod.receiveOnApplyLight(amount);
            if (target.hasPower(DarkPower.POWER_ID)) {
                //if this fails it means we made another power with LightPower's ID and that breaks things anyway
                DarkPower currentDarkPower = (DarkPower)target.getPower(DarkPower.POWER_ID);
                int currentDarkPowerStacks = currentDarkPower.amount;

                //currently prioritizing semantics of behavior over not repeating a couple lines, can rewrite if needed
                if (currentDarkPowerStacks == amount) {
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, source, currentDarkPower));
                    AbstractDungeon.actionManager.addToTop(new DamageRandomEnemyAction(new DamageInfo(target, currentDarkPowerStacks * 2, DarkPower.DARK_DAMAGE_TYPE), AbstractGameAction.AttackEffect.POISON));
                }
                else if (currentDarkPowerStacks > amount) {
                    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, source, DarkPower.POWER_ID, amount));
                    AbstractDungeon.actionManager.addToTop(new DamageRandomEnemyAction(new DamageInfo(target, amount * 2, DarkPower.DARK_DAMAGE_TYPE), AbstractGameAction.AttackEffect.POISON));
                }
                else { //if (currentDarkPowerStacks < amount)
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new LightPower(target, source, amount - currentDarkPowerStacks), amount - currentDarkPowerStacks));
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, source, currentDarkPower));
                    AbstractDungeon.actionManager.addToTop(new DamageRandomEnemyAction(new DamageInfo(target, currentDarkPowerStacks * 2, DarkPower.DARK_DAMAGE_TYPE), AbstractGameAction.AttackEffect.POISON));
                }

            }
            else {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new LightPower(target, source, amount), amount));
            }
        }

        tickDuration();
    }
}
