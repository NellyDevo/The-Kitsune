package kitsunemod.cards.skills;

import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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

public class ShapeMastery extends AbstractKitsuneCard implements ModalChoice.Callback{
    public static final String ID = KitsuneMod.makeID("ShapeMastery");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int BLOCK_AMOUNT = 10;

    private ModalChoice modal;

    public ShapeMastery() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.RARE, CardTarget.SELF);
        block = baseBlock = BLOCK_AMOUNT;
        tags.add(KitsuneTags.SHAPESHIFT_CARD);
        modal = new ModalChoiceBuilder()
                .setCallback(this)
                .setColor(AbstractCardEnum.KITSUNE_COLOR)
                .addOption(EXTENDED_DESCRIPTION[0], CardTarget.SELF)
                .addOption(EXTENDED_DESCRIPTION[1], CardTarget.SELF)
                .addOption(EXTENDED_DESCRIPTION[2], CardTarget.SELF)
                .create();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        modal.open();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShapeMastery();
    }

    @Override
    public void optionSelected(AbstractPlayer p, AbstractMonster m, int option) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        switch (option) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(p, p, new HumanShapePower(p, p)));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(p, p, new KitsuneShapePower(p, p)));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(p, p, new FoxShapePower(p, p)));
                break;
            default:
                System.out.println("MODAL CHOICE ERROR");
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
