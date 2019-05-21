package kitsunemod.cards.skills;

import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class TimidAppearance extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("TimidAppearance");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/TimidAppearance.png";
    private static final int COST = 1;

    private static final int BLOCK_AMT = 7;
    private static final int UPGRADE_PLUS_BLOCK_AMT = 3;
    private static final int EMPOWERED_BLOCK_AMT = 12;
    private static final int UPGRADE_PLUS_EMPOWERED_BLOCK_AMT = 4;

    public TimidAppearance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = EMPOWERED_BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(WeakPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, magicNumber));
        } else {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        magicNumber = applyPowerOnBlockHelper(baseMagicNumber);
        isMagicNumberModified = baseMagicNumber != magicNumber;
    }


    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        magicNumber = applyPowerOnBlockHelper(baseMagicNumber);
        isMagicNumberModified = baseMagicNumber != magicNumber;
    }


    @Override
    public AbstractCard makeCopy() {
        return new TimidAppearance();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK_AMT);
            upgradeMagicNumber(UPGRADE_PLUS_EMPOWERED_BLOCK_AMT);
        }
    }
}
