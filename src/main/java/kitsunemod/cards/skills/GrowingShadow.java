package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ManuallyTriggerSoulstealAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;
import kitsunemod.powers.SoulstealPower;

public class GrowingShadow extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("GrowingShadow");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 1;


    private static final int VULNERABLE_AMT = 2;
    private static final int UPGRADE_PLUS_VULNERABLE_AMT = 2;
    private static final int DARK_AMT_TARGET = 4;
    private static final int UPGRADE_PLUS_DARK_AMT_TARGET = 2;


    public GrowingShadow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.COMMON, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = VULNERABLE_AMT;
        secondMagicNumber = baseSecondMagicNumber = DARK_AMT_TARGET;
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

        int darkToGain = secondMagicNumber + lightPlayerHas - darkPlayerHas;


        AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(p, p, darkToGain));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GrowingShadow();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE_AMT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_DARK_AMT_TARGET);
        }
    }
}
