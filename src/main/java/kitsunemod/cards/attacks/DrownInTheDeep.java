package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.DrownInTheDeepAction;
import kitsunemod.actions.MassSuggestionAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class DrownInTheDeep extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("DrownInTheDeep");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_attack.png";

    private static final int COST = -1;
    private static final int DAMAGE_AMT = 3;
    private static final int UPGRADE_DAMAGE_AMT = 1;

    public DrownInTheDeep() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (energyOnUse < EnergyPanel.totalCount) {
            energyOnUse = EnergyPanel.totalCount;
        }
        AbstractDungeon.actionManager.addToBottom(new DrownInTheDeepAction(m, freeToPlayOnce, energyOnUse, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DrownInTheDeep();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE_AMT);
        }
    }
}
