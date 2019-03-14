package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.FeedingFrenzyPower;

public class FeedingFrenzy extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("FeedingFrenzy");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 2;

    private static final int TURNS = 3;
    private static final int HEAL_AMT = 3;
    private static final int UPGRADE_PLUS_HEAL_AMT = 2;

    public FeedingFrenzy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = HEAL_AMT;
        secondMagicNumber = baseSecondMagicNumber = TURNS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FeedingFrenzyPower(p, magicNumber, secondMagicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeedingFrenzy();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HEAL_AMT);
        }
    }
}
