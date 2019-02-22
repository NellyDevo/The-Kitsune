package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.HumanShapePower;

public class AncientMalice extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("AncientMalice");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";


    private static final int COST = 2;

    private static final int BASE_DISCARD = 1;
    private static final int ELDER_TIER_UPGRADE_CARD_DISCARD = 1;
    private static final int ELDER_TIER_DARK_GAIN_REQUIREMENT = 50;

    public AncientMalice() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = BASE_DISCARD;
        elderNumber = baseElderNumber = ELDER_TIER_DARK_GAIN_REQUIREMENT;
    }

    public AncientMalice(int timesUpgraded) {
        this();
        initializeWithUpgrades(timesUpgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int tmp = magicNumber;
        tmp = Math.min(tmp, p.hand.size());
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, tmp, false));
        if (p.hasPower(HumanShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, tmp));
        }
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
    public void onApplyDark(int amount) {
        int tempTimesUpgraded = this.timesUpgraded;
        misc += amount;
        upgrade();
        if (timesUpgraded != tempTimesUpgraded) {
            playUpgradeVfx();
        }
    }

    @Override
    protected boolean allCondition() {
        return misc >= (timesUpgraded + 1) * baseElderNumber;
    }

    @Override
    public void upgradeAll() {
        upgradeName();
        upgradeMagicNumber(ELDER_TIER_UPGRADE_CARD_DISCARD);
    }

    @Override
    public void upgradeName() {
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }

    @Override
    public AbstractCard makeCopy() {
        return new AncientMalice(timesUpgraded);
    }

    @Override
    public void upgrade1() {
        super.upgrade1();
        rawDescription = EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }
}
