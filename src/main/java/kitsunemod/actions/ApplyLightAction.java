package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;

public class ApplyLightAction extends AbstractGameAction {
    private AbstractCreature target;


    public ApplyLightAction(
            final AbstractCreature source,
            final AbstractCreature target,
            final int stacks)

    {
        this.source = source;
        this.target = target;
        this.amount = stacks;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
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
            }

        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new LightPower(target, source, amount), amount));
        }
        isDone = true;
    }
}
