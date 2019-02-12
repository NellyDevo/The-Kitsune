package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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
import kitsunemod.powers.*;

public class LashOut extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("LashOut");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/LashOut.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 8;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int KITSUNE_ASPECT_SHADOW = 1;
    private static final int HUMAN_ASPECT_BLOCK = 12;

    public LashOut() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        block = baseBlock = HUMAN_ASPECT_BLOCK;
        magicNumber = baseMagicNumber = KITSUNE_ASPECT_SHADOW;
        exhaustOnUseOnce = true;
        tags.add(KitsuneTags.ASPECT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //todo: move this to a LashOutAction so its easier to add fx and stuff
        boolean isFox = false;
        boolean isKitsune = false;
        boolean isHuman = false;

        if (p.hasPower(NinetailedShapePower.POWER_ID)) {
            isFox = isKitsune = isHuman = true;
        }
        else if (p.hasPower(FoxShapePower.POWER_ID)) {
            isFox = true;
        }
        else if (p.hasPower(KitsuneShapePower.POWER_ID)) {
            isKitsune = true;
        }
        else if (p.hasPower(HumanShapePower.POWER_ID)){
            isHuman = true;
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAction(m,new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));

        if (isFox) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        }
        if (isKitsune) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new InTheShadowsPower(p, magicNumber, InTheShadowsPower.DEFAULT_HEAL_AMOUNT)));
        }
        if (isHuman) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LashOut();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
