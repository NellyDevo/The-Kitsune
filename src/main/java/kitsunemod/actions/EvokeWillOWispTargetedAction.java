package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import kitsunemod.orbs.WillOWisp;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;

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
