package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.NinetailedShapePower;

public class LieInWait extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("LieInWait");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 1;

    private static final int HP_LOSS = 3;
    private static final int UPGRADE_PLUS_HP_LOSS = 3;
    private static final int ENERGY_GAIN = 2;


    public LieInWait() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = HP_LOSS;
        secondMagicNumber = baseSecondMagicNumber = ENERGY_GAIN;
        rawDescription = getRawDescription();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(secondMagicNumber));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ConservePower(p, 1), 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LieInWait();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HP_LOSS);
            rawDescription = getRawDescription();
            initializeDescription();
        }
    }

    private String getRawDescription() {
        String description = DESCRIPTION;
        String energyString = "";
        if (secondMagicNumber == 1) {
            energyString += cardStrings.EXTENDED_DESCRIPTION[0];
        }
        else if (secondMagicNumber > 1) {
            energyString += cardStrings.EXTENDED_DESCRIPTION[0];
            for (int i = 0; i < secondMagicNumber - 1; i++) {
                energyString += cardStrings.EXTENDED_DESCRIPTION[1];
            }
        }
        description += energyString;
        if (!upgraded) {
            description += cardStrings.EXTENDED_DESCRIPTION[2];
        }
        else
        {
            description += cardStrings.EXTENDED_DESCRIPTION[2];
            description += cardStrings.EXTENDED_DESCRIPTION[3];
        }
        return description;
    }
}
