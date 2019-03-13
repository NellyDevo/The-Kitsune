package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.BalancingActPower;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;


public class ApplyDarkAction extends AbstractGameAction {

    public ApplyDarkAction(
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
            KitsuneMod.receiveOnApplyDark(amount);
            if (target.hasPower(LightPower.POWER_ID)) {
                //if this fails it means we made another power with DarkPower's ID and that breaks things anyway
                LightPower currentLightPower = (LightPower)target.getPower(LightPower.POWER_ID);
                int currentLightPowerStacks = currentLightPower.amount;

                //currently prioritizing semantics of behavior over not repeating a couple lines, can rewrite if needed
                if (currentLightPowerStacks == amount) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(target, target, currentLightPowerStacks * 2));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, currentLightPower));
                }
                else if (currentLightPowerStacks > amount) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(target, target, amount * 2));
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(target, source, LightPower.POWER_ID, amount));
                }
                else { //if (currentDarkPowerStacks < amount)
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(target, target, currentLightPowerStacks * 2));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, currentLightPower));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new DarkPower(target, source, amount - currentLightPowerStacks), amount - currentLightPowerStacks));
                    if (target.hasPower(BalancingActPower.POWER_ID)) {
                        pseudoTriggerLight(target.getPower(BalancingActPower.POWER_ID));
                    }
                }

            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new DarkPower(target, source, amount), amount));
            }
            isDone = true;
        }

        tickDuration();
    }

    private void pseudoTriggerLight(AbstractPower power) {
        power.flash();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(target, target, power.amount));
    }
}
