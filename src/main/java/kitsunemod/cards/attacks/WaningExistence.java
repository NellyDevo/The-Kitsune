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
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.SoulstealPower;

public class WaningExistence extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("WaningExistence");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/CleansingNova.png";
    private static final int COST = 1;

    private static final int DAMAGE_AMT = 6;

    public WaningExistence() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int tmp = baseDamage;
        if (mo.hasPower(SoulstealPower.POWER_ID)) {
            baseDamage += mo.getPower(SoulstealPower.POWER_ID).amount * (upgraded? 2 : 1);
        }
        super.calculateCardDamage(mo);
        baseDamage = tmp;
        isDamageModified = baseDamage != damage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new WaningExistence();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
