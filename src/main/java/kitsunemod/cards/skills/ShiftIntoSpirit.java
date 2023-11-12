package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.patches.AbstractCardEnum;

public class ShiftIntoSpirit extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("ShiftIntoSpirit");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/ShiftIntoSpirit.png";

    private static final int COST = 1;

    private static final int BASE_BLOCK = 6;
    private static final int ELDER_TIER_UPGRADE_BLOCK = 2;
    private static final int ELDER_TIER_QUESTION_ROOM_REQUIREMENT = 1;
    private static final int HEAL_AMOUNT = 10;

    public ShiftIntoSpirit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        elderNumber = baseElderNumber = ELDER_TIER_QUESTION_ROOM_REQUIREMENT;
        block = baseBlock = BASE_BLOCK;
     
        magicNumber = baseMagicNumber = HEAL_AMOUNT;
    }
    public ShiftIntoSpirit(int timesUpgraded, int misc) {
        this();
        this.misc = misc;
        initializeWithUpgrades(timesUpgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if (timesUpgraded >= 9) {
            AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, magicNumber));
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
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof EventRoom && !upgradedThisRoom) {
            ++misc;
            int tmpUpgrades = timesUpgraded;
            upgrade();
            if (tmpUpgrades != timesUpgraded) {
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
        upgradeBlock(ELDER_TIER_UPGRADE_BLOCK);
    }

    @Override
    public void upgradeName() {
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShiftIntoSpirit(timesUpgraded, misc);
    }
}
