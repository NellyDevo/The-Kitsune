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
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class AncientMalice extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("AncientMalice");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_attack.png";


    private static final int COST = 2;

    private static final int DAMAGE_AMT = 13;
    private static final int UPGRADE_DAMAGE_AMT = 5;

    public AncientMalice() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void applyPowers() {
        int tmp = baseDamage;
        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
            baseDamage += AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
        }
        super.applyPowers();
        baseDamage = tmp;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int tmp = baseDamage;
        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
            baseDamage += AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
        }
        super.calculateCardDamage(mo);
        if (mo.hasPower(VulnerablePower.POWER_ID)) {
            damage = (int)mo.getPower(VulnerablePower.POWER_ID).atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
        }
        baseDamage = tmp;
    }

    @Override
    public AbstractCard makeCopy() {
        return new AncientMalice();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }
}
