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
import kitsunemod.actions.ManuallyTriggerSoulstealAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.SoulstealPower;

public class EssenceTheft extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("EssenceTheft");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/EssenceTheft.png";
    private static final int COST = 1;

    private static final int UPGRADED_COST = 0;
    private static final int SOULSTEAL_AMT = 2;

    public EssenceTheft() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = SOULSTEAL_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new SoulstealPower(m, p, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
        AbstractDungeon.actionManager.addToBottom(new ManuallyTriggerSoulstealAction(m, p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EssenceTheft();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
