package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;
import kitsunemod.powers.SoulstealPower;

public class ManuallyTriggerSoulstealAction extends AbstractGameAction {


    public ManuallyTriggerSoulstealAction(
            final AbstractCreature target,
            final AbstractCreature source)

    {
        this.source = source;
        this.target = target;
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            if (!source.isPlayer) {
                isDone = true;
                return;
            }
            if (target.hasPower(SoulstealPower.POWER_ID)) {
                SoulstealPower soulstealPower = (SoulstealPower)target.getPower(SoulstealPower.POWER_ID);
                soulstealPower.applySoulsteal(source, false);
            }
            isDone = true;
        }
        tickDuration();
    }
}
