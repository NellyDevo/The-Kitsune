package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ApplyLightAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class GlaresShadow extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("GlaresShadow");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/GlaresShadow.png";

    private static final int COST = 2;
    private static final int POWER_AMOUNT = 9;
    private static final int UPGRADE_POWER_AMOUNT = 4;

    public GlaresShadow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = POWER_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(p, p, POWER_AMOUNT));
        AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(p, p, POWER_AMOUNT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GlaresShadow();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_POWER_AMOUNT);
        }
    }
}
