package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScrapeEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.SoulstealPower;

public class RendSpirit extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("RendSpirit");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/RendSpirit.png";
    private static final int COST = 3;

    private static final int ATTACK_DMG = 35;
    private static final int UPGRADE_PLUS_DMG = 10;

    private static final int BASE_HP_LOSS = 10;
    private static final int HP_LOSS_REDUCTION_MULTIPLIER = 1;

    public RendSpirit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = BASE_HP_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ScrapeEffect(m.hb.cX, m.hb.cY)));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        int hpLoss = magicNumber;
        if (m.hasPower(SoulstealPower.POWER_ID)) {
            hpLoss -= m.getPower(SoulstealPower.POWER_ID).amount * HP_LOSS_REDUCTION_MULTIPLIER;
        }
        if (hpLoss < 0) {
            hpLoss = 0;
        }
        if (hpLoss > 0) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, hpLoss));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RendSpirit();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
