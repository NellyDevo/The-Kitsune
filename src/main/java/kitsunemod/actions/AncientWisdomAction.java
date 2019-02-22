package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class AncientWisdomAction extends AbstractGameAction {

    public AncientWisdomAction(int amount) {
        this.amount = amount;
    }
    
    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int tmp = p.hand.size();
        ArrayList<AbstractCard> list = new ArrayList<>();
        if (!(amount >= tmp)) {
            for (int i = 0; i < amount; ++i) {
                AbstractCard randomCard = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                while (list.contains(randomCard)) {
                    randomCard = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                }
                list.add(randomCard);
            }
        } else {
            list.addAll(p.hand.group);
        }
        int darkAmount = 0;
        for (AbstractCard card : list) {
            card.flash();
            darkAmount += card.costForTurn;
        }
        AbstractDungeon.actionManager.addToTop(new ApplyDarkAction(p, p, darkAmount));
        isDone = true;
    }
}
