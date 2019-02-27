package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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

public class Ingenuity extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("Ingenuity");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 0;

    private static final int BLOCK_AMT = 3;
    private static final int UPGRADE_PLUS_BLOCK_AMT = 1;
    private static final int BLOCK_PER_SHAPESHIFT = 2;
    private static final int UPGRADE_PLUS_SHAPESHIFT_BLOCK = 1;

    public Ingenuity() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = BLOCK_PER_SHAPESHIFT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int totalBlock = block + magicNumber * KitsuneMod.shapeshiftsThisCombat;
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, totalBlock));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Ingenuity();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK_AMT);
            upgradeMagicNumber(UPGRADE_PLUS_SHAPESHIFT_BLOCK);
        }
    }
}
