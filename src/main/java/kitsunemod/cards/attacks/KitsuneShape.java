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
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.actions.KitsuneShapeAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.CharmMonsterPower;
import kitsunemod.powers.KitsuneShapePower;

public class KitsuneShape extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("KitsuneShape");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/KitsuneShape.png";

    private static final int COST = 2;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int CHARM_TURNS = 1;
    private static final int UPGRADE_NEW_COST = 1;

    public KitsuneShape() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = CHARM_TURNS;
        exhaust = true;
        tags.add(KitsuneTags.SHAPESHIFT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new KitsuneShapeAction(m, p, magicNumber, new DamageInfo(p, damage, damageTypeForTurn)));
        AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(p, p, new KitsuneShapePower(p, p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new KitsuneShape();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBaseCost(UPGRADE_NEW_COST);
        }
    }
}
