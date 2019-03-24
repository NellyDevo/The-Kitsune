package kitsunemod.cards.skills;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
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
import kitsunemod.powers.ShadeExtraHealPower;
import kitsunemod.powers.ShadePower;

public class VanishIntoShadows extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("VanishIntoShadows");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 1;

    private static final int SHADE_STACKS = 2;

    private static final int HEAL_AMOUNT = 4;
    private static final int UPGRADE_PLUS_HEAL_AMOUNT = 2;

    public VanishIntoShadows() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = HEAL_AMOUNT;
        secondMagicNumber = baseSecondMagicNumber = SHADE_STACKS;
        this.exhaustOnUseOnce = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadePower(p, secondMagicNumber), secondMagicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadeExtraHealPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VanishIntoShadows();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            AlwaysRetainField.alwaysRetain.set(this, true);
            upgradeMagicNumber(UPGRADE_PLUS_HEAL_AMOUNT);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();

        }
    }
}
