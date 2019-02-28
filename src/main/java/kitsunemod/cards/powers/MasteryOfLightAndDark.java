package kitsunemod.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.MasteryOfLightAndDarkPower;

public class MasteryOfLightAndDark extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("MasteryOfLightAndDark");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/MasteryOfLightAndDark.png";


    private static final int COST = 1;

    private static final int BASE_POWER = 1;
    private static final int ELDER_TIER_TRIGGER_REQUIREMENT = 5;
    private static final int ELDER_TIER_POWER_UPGRADE = 1;

    public MasteryOfLightAndDark() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = BASE_POWER;
        elderNumber = baseElderNumber = ELDER_TIER_TRIGGER_REQUIREMENT;
    }

    public MasteryOfLightAndDark(int timesUpgraded, int misc) {
        this();
        this.misc = misc;
        initializeWithUpgrades(timesUpgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MasteryOfLightAndDarkPower(p, magicNumber), magicNumber));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        elderNumber = baseElderNumber * (timesUpgraded + 1) - misc;
        isElderNumberModified = elderNumber != baseElderNumber;
    }

    @Override
    public void finalizeDescription() {
        rawDescription = UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    protected boolean allCondition() {
        return misc >= (timesUpgraded + 1) * baseElderNumber;
    }

    @Override
    public void upgradeAll() {
        upgradeName();
        upgradeMagicNumber(ELDER_TIER_POWER_UPGRADE);
    }

    @Override
    public void onTriggerLight() {
        onTriggerLightOrDark();
    }

    @Override
    public void onTriggerDark() {
        onTriggerLightOrDark();
    }

    private void onTriggerLightOrDark() {
        if (!upgradedThisRoom) {
            misc++;
            int tmp = timesUpgraded;
            upgrade();
            if (tmp != timesUpgraded) {
                playUpgradeVfx();
                upgradedThisRoom = true;
            }
        }
    }

    @Override
    public void upgradeName() {
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }

    @Override
    public AbstractCard makeCopy() {
        return new MasteryOfLightAndDark(timesUpgraded, misc);
    }

    @Override
    public void upgrade1(){
        super.upgrade1();
        rawDescription = EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }
}
