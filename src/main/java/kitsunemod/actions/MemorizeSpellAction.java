package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.cards.powers.MemorizeSpell;
import kitsunemod.powers.MemorizeSpellPower;

public class MemorizeSpellAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean initialized = false;
    private boolean upgraded;

    public MemorizeSpellAction(boolean upgraded) {
        amount = 1;
        p = AbstractDungeon.player;
        this.upgraded = upgraded;
    }

    public void update() {
        if (initialized && !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                p.hand.addToHand(card);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MemorizeSpellPower(p, card, upgraded)));
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            isDone = true;
        }
        if (!initialized) {
            initialized = true;
            if (p.hand.isEmpty()) {
                isDone = true;
            } else if (p.hand.size() <= amount) {
                for (AbstractCard card : p.hand.group) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MemorizeSpellPower(p, card, upgraded)));
                }
                isDone = true;
            } else {
                AbstractDungeon.handCardSelectScreen.open(MemorizeSpell.EXTENDED_DESCRIPTION[0], amount, false);
            }
        }
    }
}
