package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.patches.AbstractCardEnum;

public class FeralInstinct extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("FeralInstinct");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 1;

    private static final int BLOCK_AMT = 3;
    private static final int UPGRADE_BLOCK_AMT_PER_ELDER_TIER = 1;
    private static final int ELDER_TIER_BLOCKED_REQUIREMENT = 30;

    public FeralInstinct() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
    }

    public FeralInstinct(int timesUpgraded) {
        this();
        initializeWithUpgrades(timesUpgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public void onBlockedDamage(int amount) {
        misc += amount;
        int tempTimesUpgraded = this.timesUpgraded;
        upgrade();
        if (timesUpgraded != tempTimesUpgraded) {
            playUpgradeVfx();
        }
    }

    @Override
    protected boolean allCondition() {
        return misc >= (timesUpgraded + 1) * ELDER_TIER_BLOCKED_REQUIREMENT;
    }


    @Override
    public void upgradeAll() {
        if (timesUpgraded < 9) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK_AMT_PER_ELDER_TIER);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeralInstinct(timesUpgraded);
    }

    @Override
    public void upgradeName() {
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }
}
