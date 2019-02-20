package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChannelWillOWispAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class PurityOfFunction extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("PurityOfFunction");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";

    private static final int COST = 1;
    private static final int BLOCK_MULTIPLIER = 2;
    private static final int UPGRADE_BLOCK_MULTIPLIER = 1;

    public PurityOfFunction() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = BLOCK_MULTIPLIER;
        block = baseBlock = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p.type == AbstractPower.PowerType.DEBUFF) {
                baseBlock += p.amount;
            }
        }
        baseBlock *= magicNumber;
        super.applyPowers();
        baseBlock = 0;
        isBlockModified = baseBlock != block;
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p.type == AbstractPower.PowerType.DEBUFF) {
                baseBlock += p.amount;
            }
        }
        baseBlock *= magicNumber;
        super.calculateCardDamage(mo);
        baseBlock = 0;
        isBlockModified = baseBlock != block;
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new PurityOfFunction();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_BLOCK_MULTIPLIER);
        }
    }
}
