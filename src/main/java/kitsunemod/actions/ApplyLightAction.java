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
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.BalancingActPower;
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
                //if this fails it means we made another power with DarkPower's ID and that breaks things anyway
                DarkPower currentDarkPower = (DarkPower)target.getPower(DarkPower.POWER_ID);
                int currentDarkPowerStacks = currentDarkPower.amount;

                //currently prioritizing semantics of behavior over not repeating a couple lines, can rewrite if needed
                if (currentDarkPowerStacks == amount) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, currentDarkPower));
                }
                else if (currentDarkPowerStacks > amount) {
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(target, source, DarkPower.POWER_ID, amount));
                }
                else { //if (currentDarkPowerStacks < amount)
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, currentDarkPower));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new LightPower(target, source, amount - currentDarkPowerStacks), amount - currentDarkPowerStacks));
                    if (target.hasPower(BalancingActPower.POWER_ID)) {
                        psuedoTriggerDark(target.getPower(BalancingActPower.POWER_ID));
                    }
                }

            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new LightPower(target, source, amount), amount));
            }
        }

        tickDuration();
    }

    private void psuedoTriggerDark(AbstractPower power) {
        power.flash();
        AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(target, power.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.POISON));
    }
}
