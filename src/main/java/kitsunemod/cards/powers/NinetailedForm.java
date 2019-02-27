package kitsunemod.cards.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;
import kitsunemod.powers.NinetailedShapePower;

public class NinetailedForm extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("NinetailedForm");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/NinetailedForm.png";

    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;

    public NinetailedForm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.NONE);

        isEthereal = true;
        tags.add(KitsuneTags.SHAPESHIFT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(p, p, new NinetailedShapePower(p, p,
                FoxShapePower.BONUS_STRENGTH + KitsuneShapePower.BONUS_STRENGTH + HumanShapePower.BONUS_STRENGTH,
                FoxShapePower.BONUS_DEXTERITY + KitsuneShapePower.BONUS_DEXTERITY + HumanShapePower.BONUS_DEXTERITY)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new NinetailedForm();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
