package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.cards.skills.MemorizeSpell;
import kitsunemod.powers.MemorizeSpellPower;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MemorizeSpellAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean initialized = false;
    private boolean upgraded;
    private ArrayList<AbstractCard> cannotMemorizeList;

    private int turns;
    public MemorizeSpellAction(boolean upgraded, int cards, int turns) {
        amount = cards;
        this.turns = turns;
        duration = Settings.ACTION_DUR_FAST;

        p = AbstractDungeon.player;
        this.upgraded = upgraded;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            cannotMemorizeList = p.hand.group.stream()
                    .filter(card -> !canMemorizeCard(card))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (AbstractDungeon.player.hand.group.size() == cannotMemorizeList.size()) {
                //thought bubble here
                this.isDone = true;
                return;
            } else if (AbstractDungeon.player.hand.group.size() - cannotMemorizeList.size() <= amount) {
                ArrayList<AbstractCard> memorizableCards = p.hand.group.stream()
                        .filter(this::canMemorizeCard)
                        .collect(Collectors.toCollection(ArrayList::new));
                for (AbstractCard card : memorizableCards) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MemorizeSpellPower(p, card, upgraded, turns)));
                }
                this.isDone = true;
                return;
            } else {
                p.hand.group.removeAll(cannotMemorizeList);
                if (p.hand.size() <= amount) {
                    for (AbstractCard card : p.hand.group) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MemorizeSpellPower(p, card, upgraded, turns)));
                    }
                    p.hand.group.addAll(cannotMemorizeList);
                    cannotMemorizeList = null;
                    p.hand.refreshHandLayout();
                    this.isDone = true;
                    return;
                }
                else
                {
                    AbstractDungeon.handCardSelectScreen.open(MemorizeSpell.EXTENDED_DESCRIPTION[0], amount, false);
                    tickDuration();
                    return;
                }
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                p.hand.addToTop(card);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MemorizeSpellPower(p, card, upgraded, turns)));
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            p.hand.group.addAll(cannotMemorizeList);
            cannotMemorizeList = null;
            p.hand.refreshHandLayout();
            isDone = true;
        }
    }
    private boolean canMemorizeCard(AbstractCard c) {
        return c.type == AbstractCard.CardType.ATTACK || c.type == AbstractCard.CardType.SKILL;
    }
}
