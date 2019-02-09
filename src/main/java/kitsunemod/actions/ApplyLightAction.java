package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
        target.addPower(new LightPower(target, source,this.amount));
        isDone = true;
    }
}
