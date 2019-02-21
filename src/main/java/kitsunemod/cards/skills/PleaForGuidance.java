package kitsunemod.cards.skills;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChannelWillOWispAction;
import kitsunemod.actions.WillOWispAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class PleaForGuidance extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("PleaForGuidance");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 1;

    private static final int WILL_O_WISPS = 1;
    private static final int UPGRADE_WILL_O_WISPS = 1;
    private static final int CARDS = 1;
    private static final int UPGRADE_CARDS = 1;


    public PleaForGuidance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = WILL_O_WISPS;
        secondMagicNumber = baseSecondMagicNumber = CARDS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ChannelWillOWispAction(magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PleaForGuidance();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_WILL_O_WISPS);
            upgradeSecondMagicNumber(UPGRADE_CARDS);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
