package kitsunemod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.special.QuickshapeFox;
import kitsunemod.cards.special.QuickshapeHuman;
import kitsunemod.cards.special.QuickshapeKitsune;

public class UnstableShapePower extends AbstractKitsunePower implements NonStackablePower {

    public boolean shouldUpgrade;
    private int lastFormId = 1;

    public static final String POWER_ID = KitsuneMod.makeID("UnstableShapePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public UnstableShapePower(final AbstractCreature owner, boolean shouldUpgrade, int amount) {

        this.owner = owner;
        this.shouldUpgrade = shouldUpgrade;
        this.amount = amount;

        name = NAME;
        ID = POWER_ID;
        isTurnBased = false;
        canGoNegative = false;
        type = PowerType.BUFF;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("closeUp");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();

        int cardType = 3;
        do {
            cardType = AbstractDungeon.cardRandomRng.random(2);
        } while (cardType == 3 || cardType == lastFormId);

        lastFormId = cardType;
        String cardKey = "";
        switch (cardType) {
            case 0:
                cardKey = QuickshapeFox.ID;
                break;
            case 1:
                cardKey = QuickshapeKitsune.ID;
                break;
            case 2:
                cardKey = QuickshapeHuman.ID;
            default:
                KitsuneMod.logger.warn("UnstableShapePower: Shouldn't have picked a random number other than 0-2?");
        }

        AbstractCard newCard = CardLibrary.getCard(cardKey).makeCopy();
        if (shouldUpgrade) {
            newCard.upgrade();
        }

        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(newCard, amount));
    }

    @Override
    public void updateDescription() {

        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        if (shouldUpgrade) {
            description += DESCRIPTIONS[2];
        }
        if (amount == 1) {
            description += DESCRIPTIONS[3];
        }
        else {
            description += DESCRIPTIONS[4];
        }
    }
}
