package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class Serenity extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("Serenity");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/Serenity.png";
    private static final int COST = 1;

    private static final int BLOCK_AMT = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int TURN_LIMIT = 3;
    private static final int PLATED_ARMOR_AMT = 2;
    private static final int UPGRADE_PLATED_ARMOR = 1;


    public Serenity() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = PLATED_ARMOR_AMT;
        secondMagicNumber = baseSecondMagicNumber = TURN_LIMIT;
        block = baseBlock = BLOCK_AMT;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if (KitsuneMod.turnsSpentInSameShape >= secondMagicNumber) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber), magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Serenity();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLATED_ARMOR);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (KitsuneMod.turnsSpentInSameShape >= TURN_LIMIT) {
            rawDescription = DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            initializeDescription();
        }
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }
}
