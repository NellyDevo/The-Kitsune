package kitsunemod.cards.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.GatheringLightPower;

public class GatheringLight extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("GatheringLight");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_power.png";

    private static final int COST = 3;
    private static final int LIGHT_AMOUNT = 2;
    private static final int UPGRADE_LIGHT_AMOUNT = 1;
    private static final int THORNS_AMOUNT = 1;


    public GatheringLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.NONE);
        magicNumber = baseMagicNumber = LIGHT_AMOUNT;
        secondMagicNumber = baseSecondMagicNumber = THORNS_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GatheringLightPower(p, magicNumber, secondMagicNumber), magicNumber));
        if (p.hasPower(GatheringLightPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (p.hasPower(GatheringLightPower.POWER_ID)) {
                        AbstractPower power = p.getPower(GatheringLightPower.POWER_ID);
                        ((TwoAmountPower)power).amount2 += secondMagicNumber;
                        power.updateDescription();
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GatheringLight();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_LIGHT_AMOUNT);
        }
    }
}
