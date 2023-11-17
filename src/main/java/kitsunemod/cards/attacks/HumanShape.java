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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyLightAction;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.HumanShapePower;

public class HumanShape extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("HumanShape");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/HumanShape.png";

    private static final int COST = 2;
    private static final int ATTACK_DMG = 12;
    private static final int VULNERABLE_AMOUNT = 1;
    private static final int LIGHT_AMOUNT = 9;
    private static final int UPGRADE_PLUS_LIGHT_AMOUNT = 6;
    private static final int UPGRADE_PLUS_VULNERABLE_AMOUNT = 1;

    public HumanShape() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = VULNERABLE_AMOUNT;
        secondMagicNumber = baseSecondMagicNumber = LIGHT_AMOUNT;
        exhaust = true;
        tags.add(KitsuneTags.SHAPESHIFT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
        AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(p, p, secondMagicNumber));
        AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(p, p, new HumanShapePower(p, p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HumanShape();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE_AMOUNT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_LIGHT_AMOUNT);
            initializeDescription();
        }
    }
}
