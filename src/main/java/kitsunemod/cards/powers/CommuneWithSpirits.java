package kitsunemod.cards.powers;

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
import kitsunemod.powers.CommuneWithSpiritsPower;

public class CommuneWithSpirits extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("CommuneWithSpirits");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/CommuneWithSpirits.png";

    private static final int COST = 2;
    private static final int WISP_AMOUNT = 1;
    private static final int UPGRADE_WISP_AMOUNT = 1;
    private static final int DRAW_CARDS = 1;


    public CommuneWithSpirits() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.NONE);
        magicNumber = baseMagicNumber = WISP_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new CommuneWithSpiritsPower(p, magicNumber, DRAW_CARDS), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CommuneWithSpirits();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_WISP_AMOUNT);
        }
    }
}
