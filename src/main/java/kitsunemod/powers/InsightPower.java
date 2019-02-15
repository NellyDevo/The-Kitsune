package kitsunemod.powers;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.ConditionalDrawAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.patches.KitsuneTags;

public class InsightPower extends AbstractPower {


    public static final String POWER_ID = KitsuneMod.makeID("InsightPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";


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

    //partially lifted from DrawPileToHandAction in StS base code
    //may refactor into action later
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        flash();
        AbstractDungeon.actionManager.addToBottom(
                new MoveCardsAction(
                        AbstractDungeon.player.hand,
                        AbstractDungeon.player.drawPile,
                        (c) -> c.tags.contains(KitsuneTags.ASPECT_CARD),
                        1));
    }


    @Override
    public void updateDescription() {

        description = DESCRIPTIONS[0];
    }
}
