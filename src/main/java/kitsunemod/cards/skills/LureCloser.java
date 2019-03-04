package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ManuallyTriggerSoulstealAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.SoulstealPower;

public class LureCloser extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("LureCloser");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 1;

    private static final int SOULSTEAL_AMT = 3;
    private static final int UPGRADE_PLUS_SOULSTEAL_AMT = 2;

    public LureCloser() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = SOULSTEAL_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int totalSoulsteal = magicNumber;
        if (p.hasPower(WeakPower.POWER_ID)) {
            totalSoulsteal += p.getPower(WeakPower.POWER_ID).amount;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new SoulstealPower(m, p, totalSoulsteal), totalSoulsteal));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LureCloser();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SOULSTEAL_AMT);
        }
    }
}
