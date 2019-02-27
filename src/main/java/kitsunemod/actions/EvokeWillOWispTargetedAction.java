package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import kitsunemod.orbs.WillOWisp;

public class EvokeWillOWispTargetedAction extends AbstractGameAction {

    private WillOWisp orb;
    public EvokeWillOWispTargetedAction(AbstractCreature target, WillOWisp orb)
    {
        this.target = target;
        this.orb = orb;

        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            orb.onEvoke(target);
        }
        tickDuration();
    }
}
