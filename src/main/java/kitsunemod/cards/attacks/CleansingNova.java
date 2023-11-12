package kitsunemod.cards.attacks;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class CleansingNova extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("CleansingNova");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/CleansingNova.png";
    private static final int COST = 1;

    private static final int BLOCK_AMT = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private boolean doDamage = false;

    public CleansingNova() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.ALL);
        block = baseBlock = BLOCK_AMT;
        damage = baseDamage = 0;
        isMultiDamage = true;

        exhaustOnUseOnce = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        if (doDamage) {
            AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(p, p));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_AWAKENED_ATTACK"));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShockWaveEffect(p.hb.cX, p.hb.cY, new Color(0.9f, 0.8f, 0.95f, 1.0f), ShockWaveEffect.ShockWaveType.CHAOTIC)));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.15f));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        } else {
            p.useShakeAnimation(0.5f);
            AbstractDungeon.actionManager.addToBottom(new SFXAction("POWER_CONSTRICTED"));
        }
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.baseDamage = AbstractDungeon.player.currentBlock + block;
        if (baseDamage > 0) {
            super.applyPowers();
            doDamage = true;
        } else {
            doDamage = false;
        }
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
        baseDamage = 0;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.baseDamage = AbstractDungeon.player.currentBlock + block;
        if (baseDamage > 0) {
            super.calculateCardDamage(mo);
            doDamage = true;
        } else {
            doDamage = false;
        }
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
        baseDamage = 0;
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new CleansingNova();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
