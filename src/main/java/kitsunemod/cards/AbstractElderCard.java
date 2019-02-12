package kitsunemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class AbstractElderCard extends AbstractKitsuneCard {

    public AbstractElderCard(String id, String name, String img, int cost, String rawDescription,
                             CardType type, CardColor color,
                             CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
        }
        if (timesUpgraded < 9) {
            upgrade();
        }
    }

    protected void upgrade1() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade1();
        }
        timesUpgraded = 1;
        upgradeAll();
    }

    protected void upgrade2() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade2();
        }
        timesUpgraded = 2;
        upgradeAll();
    }

    protected void upgrade3() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade3();
        }
        timesUpgraded = 3;
        upgradeAll();
    }

    protected void upgrade4() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade4();
        }
        timesUpgraded = 4;
        upgradeAll();
    }

    protected void upgrade5() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade5();
        }
        timesUpgraded = 5;
        upgradeAll();
    }

    protected void upgrade6() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade6();
        }
        timesUpgraded = 6;
        upgradeAll();
    }

    protected void upgrade7() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade7();
        }
        timesUpgraded = 7;
        upgradeAll();
    }

    protected void upgrade8() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade8();
        }
        timesUpgraded = 8;
        upgradeAll();
    }

    protected void upgrade9() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.misc = this.misc;
            masterDeckCopy.upgrade9();
        }
        timesUpgraded = 9;
        upgradeAll();
    }

    protected void upgradeAll() {
        upgradeName();
    }

    protected boolean allCondition() {
        return false;
    }

    protected boolean firstCondition() {
        return allCondition();
    }

    protected boolean secondCondition() {
        return allCondition();
    }

    protected boolean thirdCondition() {
        return allCondition();
    }

    protected boolean fourthCondition() {
        return allCondition();
    }

    protected boolean fifthCondition() {
        return allCondition();
    }

    protected boolean sixthCondition() {
        return allCondition();
    }

    protected boolean seventhCondition() {
        return allCondition();
    }

    protected boolean eighthCondition() {
        return allCondition();
    }

    protected boolean ninthCondition() {
        return allCondition();
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

    public void onCardDrawn(AbstractCard card, boolean isExtraDraw) { }

    public void onBlockedDamage(int amount) { }

    public void onLoseHp(DamageInfo info) { }

    public void onEnterRoom(AbstractRoom room) {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
        if (firstCondition() && timesUpgraded >= 0) {
            upgrade1();
        }
        if (secondCondition() && timesUpgraded >= 1) {
            upgrade2();
        }
        if (thirdCondition() && timesUpgraded >= 2) {
            upgrade3();
        }
        if (fourthCondition() && timesUpgraded == 3) {
            upgrade4();
        }
        if (fifthCondition() && timesUpgraded == 4) {
            upgrade5();
        }
        if (sixthCondition() && timesUpgraded == 5) {
            upgrade6();
        }
        if (seventhCondition() && timesUpgraded == 6) {
            upgrade7();
        }
        if (eighthCondition() && timesUpgraded == 7) {
            upgrade8();
        }
        if (ninthCondition() && timesUpgraded == 8) {
            upgrade9();
        }
    }

    protected void initializeWithUpgrades(int amount) {
        if (amount >= 1) {
            upgrade1();
        }
        if (amount >= 2) {
            upgrade2();
        }
        if (amount >= 3) {
            upgrade3();
        }
        if (amount >= 4) {
            upgrade4();
        }
        if (amount >= 5) {
            upgrade5();
        }
        if (amount >= 6) {
            upgrade6();
        }
        if (amount >= 7) {
            upgrade7();
        }
        if (amount >= 8) {
            upgrade8();
        }
        if (amount >= 9) {
            upgrade9();
        }
    }
}
