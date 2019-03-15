package kitsunemod.cards.basic;

import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.cards.basic.changeshapeoptions.FoxShapeOption;
import kitsunemod.cards.basic.changeshapeoptions.HumanShapeOption;
import kitsunemod.cards.basic.changeshapeoptions.KitsuneShapeOption;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;

public class ChangeShape extends AbstractKitsuneCard implements ModalChoice.Callback{
    public static final String ID = KitsuneMod.makeID("ChangeShape");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";

    private static final int COST = 1;

    private ModalChoice modal;

    public ChangeShape() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.BASIC, CardTarget.SELF);
        tags.add(KitsuneTags.SHAPESHIFT_CARD);
        modal = new ModalChoiceBuilder()
                .setCallback(this)
                .addOption(new FoxShapeOption())
                .addOption(new KitsuneShapeOption())
                .addOption(new HumanShapeOption())
                .create();
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        modal.open();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChangeShape();
    }

    @Override
    public void optionSelected(AbstractPlayer p, AbstractMonster m, int option) {
        //unnecessary because all the options' results are defined in the respective use() methods
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
