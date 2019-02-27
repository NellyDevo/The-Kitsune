package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

public class PressAdvantage extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("PressAdvantage");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_attack.png";
    private static final int COST = 2;

    private static final int ATTACK_DMG = 9;
    private static final int UPGRADE_PLUS_DMG_PER_TIER = 4;

    private static final int ELDER_TIER_ENEMIES_KILLED_REQUIREMENT = 5;

    public PressAdvantage() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = ELDER_TIER_ENEMIES_KILLED_REQUIREMENT;
        this.isMultiDamage = true;
    }

    public PressAdvantage(int timesUpgraded) {
        this();
        initializeWithUpgrades(timesUpgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void onMonsterDied(AbstractMonster m) {
        if (!m.hasPower("Minion")) {
            misc++;
            int tempTimesUpgraded = this.timesUpgraded;
            upgrade();
            if (timesUpgraded != tempTimesUpgraded) {
                playUpgradeVfx();
            }
        }
    }

    @Override
    protected boolean allCondition() {
        return misc >= (timesUpgraded + 1) * ELDER_TIER_ENEMIES_KILLED_REQUIREMENT;
    }

    @Override
    public void upgradeAll() {
        if (timesUpgraded < 9) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG_PER_TIER);
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
        return new PressAdvantage(timesUpgraded);
    }

}
