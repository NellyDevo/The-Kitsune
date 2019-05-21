package kitsunemod.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.CreateWillOWispAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.DivineSpiritPower;

public class DivineSpirit extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("DivineSpirit");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/DivineSpirit.png";


    private static final int COST = 1;
    private static final int WISP_COUNT = 1;
    private static final int UPGRADE_WISP_COUNT = 1;

    public DivineSpirit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = WISP_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DivineSpiritPower(p, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new CreateWillOWispAction(magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DivineSpirit();
    }

    @Override
    public void upgrade(){
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_WISP_COUNT);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
