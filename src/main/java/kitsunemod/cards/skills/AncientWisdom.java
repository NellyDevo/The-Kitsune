package kitsunemod.cards.skills;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.AncientWisdomAction;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.NinetailedShapePower;

public class AncientWisdom extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("AncientWisdom");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";


    private static final int COST = 2;

    private static final int BASE_RANDOM_CARD = 1;
    private static final int ELDER_TIER_UPGRADE_RANDOM_CARD = 1;
    private static final int ELDER_TIER_LIGHT_GAIN_REQUIREMENT = 50;

    public AncientWisdom() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        elderNumber = baseElderNumber = ELDER_TIER_LIGHT_GAIN_REQUIREMENT;
        magicNumber = baseMagicNumber = BASE_RANDOM_CARD;
        tags.add(KitsuneTags.ASPECT_CARD);
    }

    public AncientWisdom(int timesUpgraded, int misc) {
        this();
        this.misc = misc;
        initializeWithUpgrades(timesUpgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(FoxShapePower.POWER_ID) || p.hasPower(NinetailedShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ExpertiseAction(p, BaseMod.MAX_HAND_SIZE));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        }
        AbstractDungeon.actionManager.addToBottom(new AncientWisdomAction(magicNumber));
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
    public void onApplyLight(int amount) {
        if (!upgradedThisRoom) {
            misc += amount;
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
        upgradeMagicNumber(ELDER_TIER_UPGRADE_RANDOM_CARD);
    }

    @Override
    public void upgradeName() {
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }

    @Override
    public AbstractCard makeCopy() {
        return new AncientWisdom(timesUpgraded, misc);
    }

    @Override
    public void upgrade1() {
        super.upgrade1();
        rawDescription = EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }
}
