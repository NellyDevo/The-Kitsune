package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.ShadePower;

public class FoxShape extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("FoxShape");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/FoxShape.png";

    private static final int COST = 2;
    private static final int ATTACK_DMG = 2;
    private static final int DARK_AMOUNT = 9;
    private static final int WEAK_AMOUNT = 1;
    private static final int UPGRADE_PLUS_DARK_AMOUNT = 6;
    private static final int UPGRADE_PLUS_WEAK_AMOUNT = 1;

    public FoxShape() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = WEAK_AMOUNT;
        secondMagicNumber = baseSecondMagicNumber = DARK_AMOUNT;
        exhaust = true;
        tags.add(KitsuneTags.SHAPESHIFT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false)));
        AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(p, p, secondMagicNumber));
        AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(p, p, new FoxShapePower(p, p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FoxShape();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEAK_AMOUNT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_DARK_AMOUNT);
            initializeDescription();
        }
    }
}
