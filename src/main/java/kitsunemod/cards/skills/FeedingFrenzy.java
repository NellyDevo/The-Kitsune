package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ManuallyTriggerSoulstealAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.*;

public class FeedingFrenzy extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("FeedingFrenzy");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 2;

    private static final int TURNS = 3;
    private static final int HEAL_AMT = 1;
    private static final int UPGRADE_PLUS_HEAL_AMT = 1;
    private static final int STRENGTH_PLUS_AMT = 5;

    public FeedingFrenzy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);

        block = baseBlock = HEAL_AMT;
        magicNumber = baseMagicNumber = TURNS;
        secondMagicNumber = baseSecondMagicNumber = STRENGTH_PLUS_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        //we need to not re-apply the bonus strength from this if it's already there, and since we're just using a collection of powers here this is the most reliable check
        if (!p.hasPower(DelayedStrengthLossPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, secondMagicNumber),secondMagicNumber));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DelayedStrengthLossPower(p, magicNumber, secondMagicNumber),magicNumber));
        if (p.hasPower(FoxShapePower.POWER_ID) || p.hasPower(NinetailedShapePower.POWER_ID)) {
            if (p.hasPower(FeedingFrenzyPower.POWER_ID) && upgraded) {
                FeedingFrenzyPower current = (FeedingFrenzyPower)p.getPower(FeedingFrenzyPower.POWER_ID);
                applyPowers();

                current.flash();
                current.stackPower(magicNumber);
                current.amount2 = block;
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FeedingFrenzyPower(p, block, magicNumber), magicNumber));
            }

        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeedingFrenzy();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_PLUS_HEAL_AMT);
        }
    }
}
