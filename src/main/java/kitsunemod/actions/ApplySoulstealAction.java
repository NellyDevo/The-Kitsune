package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.*;

import kitsunemod.powers.SoulstealPower;

public class ApplySoulstealAction extends AbstractGameAction {


    public ApplySoulstealAction(
            final AbstractCreature player,
            final AbstractCreature target,
            final int stacks)

    {
        this.source = player;
        this.target = target;
        this.amount = stacks;
        actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        target.addPower(new SoulstealPower(target, source, this.amount));
        isDone = true;
    }
}
