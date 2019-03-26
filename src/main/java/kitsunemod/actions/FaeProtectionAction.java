package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.FaeProtectionPower;
import kitsunemod.wisps.WillOWisp;

public class FaeProtectionAction extends AbstractGameAction {
    private int block;
    private int wispLoss;

    public FaeProtectionAction(int block, int wispLoss) {
        this.block = block;
        this.wispLoss = wispLoss;
        duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer p = AbstractDungeon.player;
            int tmp = KitsuneMod.wisps.size() * block;
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, p, tmp));
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new FaeProtectionPower(p, wispLoss), wispLoss));
            WillOWisp.enterShieldFormation();
        }
        tickDuration();
    }
}
