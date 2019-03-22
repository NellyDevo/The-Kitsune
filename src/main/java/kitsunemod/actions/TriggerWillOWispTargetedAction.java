package kitsunemod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.wisps.WillOWisp;

public class TriggerWillOWispTargetedAction extends AbstractGameAction {

    private WillOWisp wisp;
    public TriggerWillOWispTargetedAction(AbstractCreature target, WillOWisp wisp)
    {
        this.target = target;
        this.wisp = wisp;

        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.actionManager.addToBottom(new WillOWispAction(wisp.cX, wisp.cY, target, new DamageInfo(AbstractDungeon.player, wisp.damage, DamageInfo.DamageType.THORNS), WillOWisp.PROJECTILE_FLIGHT_TIME, wisp.color, Color.RED.cpy(), wisp, wisp.imgIndex, wisp.glowScale));
        }
        tickDuration();
    }
}
