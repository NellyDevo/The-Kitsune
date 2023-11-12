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
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.NinetailedShapePower;

public class BareFangs extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("BareFangs");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/BareFangs.png";
    private static final int COST = 0;

    private static final int ATTACK_DMG = 1;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int DARK_GAINED = 2;

    public BareFangs() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = DARK_GAINED;

        tags.add(KitsuneTags.ZERO_COST_ATTACK);
        tags.add(KitsuneTags.ASPECT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        if (p.hasPower(FoxShapePower.POWER_ID) || p.hasPower(NinetailedShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4f));
            AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(p, p, magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BareFangs();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
