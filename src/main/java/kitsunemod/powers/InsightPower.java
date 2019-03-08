package kitsunemod.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.patches.KitsuneTags;

public class InsightPower extends AbstractKitsunePower implements NonStackablePower {


    public static final String POWER_ID = KitsuneMod.makeID("InsightPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public boolean isUpgraded = false;

    public InsightPower(final AbstractCreature owner) {

        this.owner = owner;

        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("curiosity");
        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(KitsuneTags.ASPECT_CARD) || isUpgraded) {
            if (AbstractDungeon.player.hand.group.stream()
                            .filter(c -> c.tags.contains(KitsuneTags.ASPECT_CARD))
                            .count() > 0) {
                flash();
                AbstractDungeon.actionManager.addToBottom(
                        new MoveCardsAction(
                                AbstractDungeon.player.hand,
                                AbstractDungeon.player.drawPile,
                                (c) -> c.tags.contains(KitsuneTags.ASPECT_CARD),
                                1));
            }
        }
    }


    @Override
    public void updateDescription() {

        description = DESCRIPTIONS[0];
    }
}
