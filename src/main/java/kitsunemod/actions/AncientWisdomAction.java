package kitsunemod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AncientWisdomAction extends AbstractGameAction {
    
    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int tmp = p.currentBlock;
        p.loseBlock();
        AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(p, p, tmp));
    }
}
