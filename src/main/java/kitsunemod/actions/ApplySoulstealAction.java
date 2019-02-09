package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.monsters.*;

import kitsunemod.powers.SoulstealPower;

public class ApplySoulstealAction extends AbstractGameAction {
    private int stacks;
    private AbstractPlayer player;
    private AbstractMonster target;


    public ApplySoulstealAction(
            final AbstractPlayer player,
            final AbstractMonster target,
            final int stacks)

    {
        this.player = player;
        this.target = target;
        this.stacks = stacks;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        target.addPower(new SoulstealPower(target, player,this.stacks));
        isDone = true;
    }
}
