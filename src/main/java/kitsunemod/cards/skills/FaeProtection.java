package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.FaeProtectionAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class FaeProtection extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("FaeProtection");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/FaeProtection.png";
    private static final int COST = 1;

    private static final int BLOCK_GAIN = 4;
    private static final int WISP_SACRIFICE = 2;
    private static final int UPGRADE_WISP_SACRIFICE = -1;

    public FaeProtection() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_GAIN;
        magicNumber = baseMagicNumber = WISP_SACRIFICE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FaeProtectionAction(block, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FaeProtection();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_WISP_SACRIFICE);
        }
    }
}
