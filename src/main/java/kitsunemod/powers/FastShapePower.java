package kitsunemod.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.powers.FastShape;
import kitsunemod.patches.KitsuneTags;

public class FastShapePower extends AbstractKitsunePower {


    public static final String POWER_ID = KitsuneMod.makeID("FastShapePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public boolean alsoReduceCost = false;

    public FastShapePower(final AbstractCreature owner, boolean alsoReduceCost) {

        this.owner = owner;
        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        this.alsoReduceCost = alsoReduceCost;
        canGoNegative = false;
        if (alsoReduceCost) {
            amount = FastShape.UPGRADED_COST_REDUCE_AMOUNT;
        }


        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("afterImage");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        flash();
    }

    @Override
    public void reducePower(int reduceAmount) {
        flash();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.tags.contains(KitsuneTags.SHAPESHIFT_CARD) && action.exhaustCard) {
            action.exhaustCard = false;
            flash();
        }
    }

    @Override
    public void onDrawOrDiscard() {
        reduceShapeshiftCardCosts();
    }

    @Override
    public void onInitialApplication() {
        reduceShapeshiftCardCosts();
    }

    private void reduceShapeshiftCardCosts() {
        if (alsoReduceCost) {
            reduceShapeshiftCostsForGroup(AbstractDungeon.player.drawPile);
            reduceShapeshiftCostsForGroup(AbstractDungeon.player.hand);
            reduceShapeshiftCostsForGroup(AbstractDungeon.player.discardPile);
            reduceShapeshiftCostsForGroup(AbstractDungeon.player.exhaustPile);
        }
    }

    private void reduceShapeshiftCostsForGroup(CardGroup group) {
        for (AbstractCard card : group.group) {
            if (card.costForTurn > 0 && card.tags.contains(KitsuneTags.SHAPESHIFT_CARD)) {
                card.costForTurn = card.cost - amount;
                if (card.costForTurn < 0) card.costForTurn = 0;
                card.isCostModifiedForTurn = true;
            }
        }
    }


    @Override
    public void updateDescription() {

        description = DESCRIPTIONS[0];
        if (alsoReduceCost) {
            description += DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }
}
