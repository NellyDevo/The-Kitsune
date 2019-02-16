package kitsunemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.CharmMonsterPower;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;

public class TestCard extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("test_card");
    public static final String NAME = "Test Card";
    public static final String DESCRIPTION = "For testing things";
    public static final String UPGRADE_DESCRIPTION = "Still for Testing Things";
    public static final String IMG_PATH = "kitsunemod/images/cards/defend.png";
    private static final int COST = 0;

    public TestCard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.BASIC, CardTarget.ENEMY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new CharmMonsterPower(m, 3)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TestCard();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
        }
    }
}
