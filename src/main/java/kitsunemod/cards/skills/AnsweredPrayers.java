package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChannelWillOWispAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class AnsweredPrayers extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("AnsweredPrayers");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public AnsweredPrayers() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int tmp = 0;
        if (p.hasPower(WeakPower.POWER_ID)) {
            tmp = p.getPower(WeakPower.POWER_ID).amount;
        }
        AbstractDungeon.actionManager.addToBottom(new ChannelWillOWispAction(tmp));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AnsweredPrayers();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
