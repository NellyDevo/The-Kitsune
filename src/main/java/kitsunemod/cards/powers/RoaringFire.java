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
import kitsunemod.powers.InsightPower;
import kitsunemod.powers.RoaringFirePower;

public class RoaringFire extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("RoaringFire");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/Insight.png";

    private static final int COST = 1;

    private static final int ENERGY_FOR_WILL_O_WISP = 3;
    private static final int UPGRADE_MINUS_ENERGY_FOR_WILL_O_WISP = -1;

    public RoaringFire() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.NONE);
        magicNumber = baseMagicNumber = ENERGY_FOR_WILL_O_WISP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RoaringFirePower(p, magicNumber)));
        }
        else {
            if (p.hasPower(RoaringFirePower.POWER_ID)) {
                RoaringFirePower power = (RoaringFirePower)p.getPower(ID);
                power.stackPower(UPGRADE_MINUS_ENERGY_FOR_WILL_O_WISP);
                power.flash();
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RoaringFirePower(p, magicNumber)));
            }
        }

    }

    @Override
    public AbstractCard makeCopy() {
        return new RoaringFire();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MINUS_ENERGY_FOR_WILL_O_WISP);
        }
    }
}
