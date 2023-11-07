package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
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
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.NinetailedShapePower;


public class PackKill extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("PackKill");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/PackKill.png";
    private static final int COST = 1;

    private static final int ATTACK_DMG = 2;

    private static final int NORMAL_TIMES = 3;
    private static final int UPGRADE_PLUS_NORMAL_TIMES = 1;

    private static final int ASPECT_TIMES = 5;
    private static final int UPGRADE_PLUS_ASPECT_TIMES = 1;


    public PackKill() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = NORMAL_TIMES;
        secondMagicNumber = baseSecondMagicNumber = ASPECT_TIMES;
        tags.add(KitsuneTags.ASPECT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int times = magicNumber;

        if (p.hasPower(FoxShapePower.POWER_ID) || p.hasPower(NinetailedShapePower.POWER_ID)) {
            times = secondMagicNumber;
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (times > 1) {
            for (int i = 0; i < times - 1; i++) {
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PackKill();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_NORMAL_TIMES);
            upgradeMagicNumber(UPGRADE_PLUS_ASPECT_TIMES);
            initializeDescription();
        }
    }
}
