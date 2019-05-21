package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.CharmMonsterPower;

public class DominateWill extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("DominateWill");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/DominateWill.png";
    private static final int COST = 1;

    private static final int CHARM_TURNS = 4;
    private static final int UPGRADE_PLUS_CHARM_TURNS = 4;

    private static final int NON_MINION_HP_THRESHHOLD = 20;

    public DominateWill() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = CHARM_TURNS;
        secondMagicNumber = baseSecondMagicNumber = NON_MINION_HP_THRESHHOLD;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower("Minion") || m.currentHealth <= secondMagicNumber) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new CharmMonsterPower(m, magicNumber),magicNumber));
        }
        else {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0f, EXTENDED_DESCRIPTION[0], true));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DominateWill();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CHARM_TURNS);
        }
    }
}
