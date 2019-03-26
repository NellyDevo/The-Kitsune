package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.actions.common.*;
import kitsunemod.powers.SoulstealPower;

public class PettyTheftAction extends AbstractGameAction {
    private int cardAndEnergy;

    public PettyTheftAction(final AbstractCreature target, int cardAndEnergy) {
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.BLOCK;
        this.target = target;
        this.cardAndEnergy = cardAndEnergy;
    }

    @Override
    public void update() {
        if (target != null && target.hasPower(SoulstealPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, cardAndEnergy));
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(cardAndEnergy));
        }
        isDone = true;
    }
}
