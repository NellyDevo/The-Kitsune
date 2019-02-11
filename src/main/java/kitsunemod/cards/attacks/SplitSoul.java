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
import kitsunemod.actions.IgniteSilhouetteAction;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SplitSoul extends AbstractElderCard {
    public static final String ID = KitsuneMod.makeID("SplitSoul");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/strike.png";
    private static final int COST = 1;

    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG_PER_TIER = 3;

    private static final int CARDS_DRAWN = 1;

    private static final int ELDER_TIER_CARDS_DRAWN_REQUIREMENT = 5;
    private static Logger logger = LogManager.getLogger(KitsuneMod.class.getName());

    public SplitSoul() {
        this(0);
    }

    public SplitSoul(int timesUpgraded) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ALL, timesUpgraded);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        int[] damageArray = new int[AbstractDungeon.monsterList.size()];

        for (int i = 0; i < damageArray.length; i++) {
            damageArray[i] = damage;
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, damageArray, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
    }



    @Override
    public void onCardDrawn(AbstractCard card) {
        logger.info("SplitSoul: incrementing cards drawn");
        misc++;
        upgrade();
    }

    @Override
    protected boolean allCondition() {
        return misc >= (timesUpgraded + 1) * ELDER_TIER_CARDS_DRAWN_REQUIREMENT;
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
        super.upgradeName();
    }


    @Override
    public AbstractCard makeCopy() {
        logger.info("making new copy of SplitSoul with misc: " + misc);
        return new SplitSoul(timesUpgraded);
    }

}
