package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyLightAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;

public class Radiance extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("Radiance");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/Radiance.png";
    private static final int COST = 1;

    private static final int BLOCK_AMT = 4;
    private static final int UPGRADE_PLUS_BLOCK_AMT = 3;
    private static final int LIGHT_AMT_TARGET = 4;
    private static final int UPGRADE_PLUS_LIGHT_AMT_TARGET = 2;

    public Radiance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = LIGHT_AMT_TARGET;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        //one (or both) of these will always be zero, but it would make the code less readable to bother checking
        int darkPlayerHas = 0;
        int lightPlayerHas = 0;
        if (p.hasPower(LightPower.POWER_ID)) {
            lightPlayerHas = p.getPower(LightPower.POWER_ID).amount;
        }
        if (p.hasPower(DarkPower.POWER_ID)) {
            darkPlayerHas = p.getPower(DarkPower.POWER_ID).amount;
        }

        int lightToGain = magicNumber + darkPlayerHas - lightPlayerHas;

        AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(p, p, lightToGain));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Radiance();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK_AMT);
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT_AMT_TARGET);
        }
    }
}
