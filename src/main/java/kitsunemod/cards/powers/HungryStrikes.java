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
import kitsunemod.powers.HungryStrikesPower;
import kitsunemod.powers.StarvingStrikesPower;

public class HungryStrikes extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("HungryStrikes");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/HungryStrikes.png";

    private static final int COST = 1;
    private static final int POWER_AMOUNT = 1;

    public HungryStrikes() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.NONE);
        magicNumber = baseMagicNumber = POWER_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new HungryStrikesPower(p, magicNumber), magicNumber));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StarvingStrikesPower(p, magicNumber), magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HungryStrikes();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
