package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.TriggerWillOWispTargetedAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.wisps.WillOWisp;
import kitsunemod.patches.AbstractCardEnum;

public class PurifyingBlaze extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("PurifyingBlaze");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/PurifyingBlaze.png";
    private static final int COST = 1;

    private static final int UPGRADED_COST = 0;

    public PurifyingBlaze() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int wispsTriggered = 0;
        for (WillOWisp wisp : KitsuneMod.wisps) {
            AbstractDungeon.actionManager.addToBottom(new TriggerWillOWispTargetedAction(p, wisp));
            wispsTriggered++;
        }
        if (wispsTriggered == 0) {
            AbstractDungeon.effectsQueue.add(new ThoughtBubble(p.dialogX, p.dialogY, cardStrings.EXTENDED_DESCRIPTION[0], true));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedPower(p, 2*wispsTriggered), 2*wispsTriggered));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PurifyingBlaze();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
