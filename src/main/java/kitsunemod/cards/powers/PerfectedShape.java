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
import kitsunemod.powers.PerfectedShapePower;

public class PerfectedShape extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("PerfectedShape");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_power.png";

    private static final int COST = 2;
    private static final int BLOCK_PER_SAME_SHAPE_TURN = 1;
    private static final int UPGRADED_BLOCK_PER_SAME_SHAPE_TURN = 1;

    public PerfectedShape() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.NONE);
        magicNumber = baseMagicNumber = BLOCK_PER_SAME_SHAPE_TURN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PerfectedShapePower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PerfectedShape();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_BLOCK_PER_SAME_SHAPE_TURN);
        }
    }
}
