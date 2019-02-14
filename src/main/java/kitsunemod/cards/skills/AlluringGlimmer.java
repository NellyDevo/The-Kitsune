package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import kitsunemod.powers.SoulstealPower;

public class AlluringGlimmer extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("AlluringGlimmer");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/defend.png";
    private static final int COST = 1;

    private static final int LIGHT_AMT = 4;
    private static final int UPGRADED_LIGHT_PLUS_AMT = 2;

    private static final int SOULSTEAL_AMT = 2;
    private static final int UPGRADED_SOULSTEAL_PLUS_AMT = 1;

    public AlluringGlimmer() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = LIGHT_AMT;
        secondMagicNumber = baseSecondMagicNumber = SOULSTEAL_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(p, p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new SoulstealPower(m, p, secondMagicNumber), secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AlluringGlimmer();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_LIGHT_PLUS_AMT);
            upgradeSecondMagicNumber(UPGRADED_SOULSTEAL_PLUS_AMT);
        }
    }
}
