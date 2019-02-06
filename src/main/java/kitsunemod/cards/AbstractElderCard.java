package kitsunemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class AbstractElderCard extends AbstractKitsuneCard {
    public static String ID;
    public static CardStrings cardStrings;
    public static String NAME;
    public static String DESCRIPTION;
    public static String[] EXTRA_DESCRIPTIONS;


    public AbstractElderCard(String id, String name, String img, int cost, String rawDescription,
                             CardType type, CardColor color,
                             CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    private static void initializeStrings() {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        EXTRA_DESCRIPTIONS = cardStrings.EXTENDED_DESCRIPTION;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (timesUpgraded < 9) {
            upgrade();
        }
    }

    protected void upgrade1() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade1();
        }
        upgradeName();
    }

    protected void upgrade2() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade2();
        }
        upgradeName();
    }

    protected void upgrade3() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade3();
        }
        upgradeName();
    }

    protected void upgrade4() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade4();
        }
        upgradeName();
    }

    protected void upgrade5() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade5();
        }
        upgradeName();
    }

    protected void upgrade6() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade6();
        }
        upgradeName();
    }

    protected void upgrade7() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade7();
        }
        upgradeName();
    }

    protected void upgrade8() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade8();
        }
        upgradeName();
    }

    protected void upgrade9() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.upgrade9();
        }
        upgradeName();
    }

    protected boolean firstCondition() {
        return timesUpgraded > 0;
    }

    protected boolean secondCondition() {
        return timesUpgraded > 1;
    }

    protected boolean thirdCondition() {
        return timesUpgraded > 2;
    }

    protected boolean fourthCondition() {
        return timesUpgraded > 3;
    }

    protected boolean fifthCondition() {
        return timesUpgraded > 4;
    }

    protected boolean sixthCondition() {
        return timesUpgraded > 5;
    }

    protected boolean seventhCondition() {
        return timesUpgraded > 6;
    }

    protected boolean eighthCondition() {
        return timesUpgraded > 7;
    }

    protected boolean ninthCondition() {
        return timesUpgraded > 8;
    }

    private AbstractElderCard getMasterDeckEquivalent(AbstractCard card) {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (!c.uuid.equals(this.uuid)) {
                continue;
            }
            if (c instanceof AbstractElderCard) {
                return (AbstractElderCard) c;
            } else {
                System.out.println("AbstractElderCard: Warning: master deck copy is not an AbstractElderCard. How?");
            }
        }
        return null;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgradeName() {
        ++timesUpgraded;
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }

    @Override
    public void upgrade() {
        switch (timesUpgraded) {
            case 0:
                if (firstCondition()) {
                    upgrade1();
                }
                break;
            case 1:
                if (secondCondition()) {
                    upgrade2();
                }
                break;
            case 2:
                if (thirdCondition()) {
                    upgrade3();
                }
                break;
            case 3:
                if (fourthCondition()) {
                    upgrade4();
                }
                break;
            case 4:
                if (fifthCondition()) {
                    upgrade5();
                }
                break;
            case 5:
                if (sixthCondition()) {
                    upgrade6();
                }
                break;
            case 6:
                if (seventhCondition()) {
                    upgrade7();
                }
                break;
            case 7:
                if (eighthCondition()) {
                    upgrade8();
                }
                break;
            case 8:
                if (ninthCondition()) {
                    upgrade9();
                }
                break;
            default:
                System.out.println("AbstractElderCard: Warning: card is already upgraded 9 times, how did we get here?");
                break;
        }
    }
}
