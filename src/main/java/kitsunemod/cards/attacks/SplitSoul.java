package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.patches.AbstractCardEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SplitSoul extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("SplitSoul");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/SplitSoul.png";
    private static final int COST = 1;

    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG_PER_TIER = 3;

    private static final int ELDER_TIER_CARDS_DRAWN_REQUIREMENT = 9;

    public SplitSoul() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ALL);
        damage = baseDamage = ATTACK_DMG;
        isMultiDamage = true;
        elderNumber = baseElderNumber = ELDER_TIER_CARDS_DRAWN_REQUIREMENT;
    }

    public SplitSoul(int timesUpgraded, int misc) {
        this();
        this.misc = misc;
        initializeWithUpgrades(timesUpgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
        incrementElder();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        elderNumber = baseElderNumber * (timesUpgraded + 1) - misc;
        isElderNumberModified = elderNumber != baseElderNumber;
    }

    @Override
    public void onCardDrawn(AbstractCard card, boolean isExtraDraw) {
        if (isExtraDraw) {
            incrementElder();
        }
    }

    private void incrementElder() {
        if (!upgradedThisRoom) {
            misc++;
            int tempTimesUpgraded = this.timesUpgraded;
            upgrade();
            if (timesUpgraded != tempTimesUpgraded) {
                playUpgradeVfx();
                upgradedThisRoom = true;
            }
        }

    }

    @Override
    protected boolean allCondition() {
        return misc >= (timesUpgraded + 1) * baseElderNumber;
    }

    @Override
    public void upgradeAll() {
        upgradeName();
        upgradeDamage(UPGRADE_PLUS_DMG_PER_TIER);
    }

    @Override
    public void upgradeName() {
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }

    @Override
    public void finalizeDescription() {
        rawDescription = UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new SplitSoul(timesUpgraded, misc);
    }

}
