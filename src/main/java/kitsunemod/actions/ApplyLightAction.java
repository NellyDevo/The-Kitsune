package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;
import kitsunemod.powers.SoulstealPower;

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
                target.powers.remove(currentDarkPower);
            }
            else if (currentDarkPowerStacks > amount) {
                currentDarkPower.amount -= amount;
                currentDarkPower.flash();
            }
            else { //if (currentDarkPowerStacks < amount)
                target.powers.remove(currentDarkPower);
                target.addPower(new LightPower(target, source, amount - currentDarkPowerStacks));
            }

        }
        else {
            target.addPower(new LightPower(target, source,this.amount));
        }
        isDone = true;
    }
}
