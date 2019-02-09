package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javafx.scene.effect.Light;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;

public class ApplyDarkAction extends AbstractGameAction {
    private AbstractCreature target;


    public ApplyDarkAction(
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
        if (target.hasPower(LightPower.POWER_ID)) {
            //if this fails it means we made another power with DarkPower's ID and that breaks things anyway
            LightPower currentLightPower = (LightPower)target.getPower(LightPower.POWER_ID);
            int currentLightPowerStacks = currentLightPower.amount;

            //currently prioritizing semantics of behavior over not repeating a couple lines, can rewrite if needed
            if (currentLightPowerStacks == amount) {
                target.powers.remove(currentLightPower);
            }
            else if (currentLightPowerStacks > amount) {
                currentLightPower.amount -= amount;
                currentLightPower.flash();
            }
            else { //if (currentDarkPowerStacks < amount)
                target.powers.remove(currentLightPower);
                target.addPower(new DarkPower(target, source, amount - currentLightPowerStacks));
            }

        }
        else {
            target.addPower(new DarkPower(target, source,this.amount));
        }
        isDone = true;
    }
}
