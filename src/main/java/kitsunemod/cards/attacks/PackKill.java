package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
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
import org.omg.CORBA.PRIVATE_MEMBER;

public class PackKill extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("PackKill");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_attack.png";
    private static final int COST = 1;

    private static final int ATTACK_DMG = 5;
    private static final int UPGRADE_PLUS_DMG = 1;

    private static final int ASPECT_SECOND_ATTACK_DMG = 4;
    private static final int ASPECT_THIRD_ATTACK_DMG = 2;

    public PackKill() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = ASPECT_SECOND_ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = ASPECT_THIRD_ATTACK_DMG;
        tags.add(KitsuneTags.ASPECT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (p.hasPower(FoxShapePower.POWER_ID) || p.hasPower(NinetailedShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.75f));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            if (upgraded) {
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.33f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, secondMagicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            }
        }
    }
    @Override
    public void applyPowers() {
        int currentBaseDamage = baseDamage;
        int currentMagicNumber = baseMagicNumber;
        int currentSecondMagicNumber = baseSecondMagicNumber;
        baseDamage = currentMagicNumber;
        super.applyPowers(); // takes baseDamage and applies things like Strength or Pen Nib to set damage

        magicNumber = damage; // magic number holds the first condition's modified damage, so !M! will work
        isMagicNumberModified = magicNumber != baseMagicNumber;

        // repeat for third value
        baseDamage = currentSecondMagicNumber;
        super.applyPowers();

        secondMagicNumber = damage;
        isSecondMagicNumberModified= secondMagicNumber != baseSecondMagicNumber;

        baseDamage = currentBaseDamage;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int currentBaseDamage = baseDamage;
        int currentMagicNumber = baseMagicNumber;
        int currentSecondMagicNumber = baseSecondMagicNumber;
        baseDamage = currentMagicNumber;
        super.calculateCardDamage(mo); // takes baseDamage and applies things like Vulnerable

        magicNumber = damage; // magic number holds the first condition's modified damage, so !M! will work
        isMagicNumberModified = magicNumber != baseMagicNumber;

        // repeat for third value
        baseDamage = currentSecondMagicNumber;
        super.calculateCardDamage(mo);

        secondMagicNumber = damage;
        isSecondMagicNumberModified= secondMagicNumber != baseSecondMagicNumber;

        baseDamage = currentBaseDamage;
        super.calculateCardDamage(mo);

    }

    @Override
    public AbstractCard makeCopy() {
        return new PackKill();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
