package kitsunemod.cards;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import kitsunemod.KitsuneMod;

public abstract class AbstractElderCard extends AbstractKitsuneCard {
    public int elderNumber;
    public int baseElderNumber;
    public boolean isElderNumberModified;
    public boolean upgradedElderNumber;
    public boolean upgradedThisRoom;

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
        }
        if (timesUpgraded < 9) {
            upgrade();
        }
    }

    protected void upgrade1() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade1();
        }
        timesUpgraded = 1;
        upgradeAll();
    }

    protected void upgrade2() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade2();
        }
        timesUpgraded = 2;
        upgradeAll();
    }

    protected void upgrade3() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade3();
        }
        timesUpgraded = 3;
        upgradeAll();
    }

    protected void upgrade4() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade4();
        }
        timesUpgraded = 4;
        upgradeAll();
    }

    protected void upgrade5() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade5();
        }
        timesUpgraded = 5;
        upgradeAll();
    }

    protected void upgrade6() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade6();
        }
        timesUpgraded = 6;
        upgradeAll();
    }

    protected void upgrade7() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade7();
        }
        timesUpgraded = 7;
        upgradeAll();
    }

    protected void upgrade8() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade8();
        }
        timesUpgraded = 8;
        upgradeAll();
    }

    protected void upgrade9() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null && !AbstractDungeon.player.masterDeck.contains(this)) {
            masterDeckCopy.upgrade9();
        }
        timesUpgraded = 9;
        upgradeAll();
        finalizeDescription();
    }

    protected void finalizeDescription() {
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
        if (AbstractDungeon.player == null) {
            return null;
        }
        if (AbstractDungeon.player.masterDeck == null) {
            return null;
        }
        if (AbstractDungeon.player.masterDeck.size() == 0) {
            return null;
        }
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

    public int onLoseHp(DamageInfo info, int finalAmount) { return finalAmount; }

    public void onEnterRoom(AbstractRoom room) {

    }

    public void onBlockedDamage(int amount) {

    }

    public void onApplyDark(int amount) {

    }

    public void onApplyLight(int amount) {

    }

    public void onTriggerLight() {

    }

    public void onTriggerDark() {

    }

    public void onMonsterDied(AbstractMonster m) {

    }

    public void onPostBattleOrRoomEntered() {
        upgradedThisRoom = false;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
        AbstractElderCard masterDeckCopy = getMasterDeckEquivalent(this);
        if (masterDeckCopy != null) {
            masterDeckCopy.misc = this.misc;
        }
        if (firstCondition() && timesUpgraded == 0) {
            upgrade1();
        }
        if (secondCondition() && timesUpgraded == 1) {
            upgrade2();
        }
        if (thirdCondition() && timesUpgraded == 2) {
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

    //call this in the hook that calls upgrade() if the upgrade went through
    //attaching it directly into upgradeAll or such causes an infinite loop when makeCopy gets called
    //or rewrite this to suppress playing the vfx during initialization, either way is vaguely ugly imo
    protected void playUpgradeVfx() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F)));
            AbstractDungeon.actionManager.addToTop(new VFXAction(new ShowCardBrieflyEffect(makeStatEquivalentCopy())));
        }
        else {
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(makeStatEquivalentCopy()));
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }

    public static class ElderNumber extends DynamicVariable {

        @Override
        public int baseValue(AbstractCard card) {
            if (card instanceof AbstractElderCard) {
                return ((AbstractElderCard)card).baseElderNumber;
            } else {
                return -1;
            }
        }

        @Override
        public boolean isModified(AbstractCard card) {
            if (card instanceof AbstractElderCard) {
                return ((AbstractElderCard)card).isElderNumberModified;
            } else {
                return false;
            }
        }

        @Override
        public void setIsModified(AbstractCard card, boolean v) {
            if (card instanceof AbstractElderCard) {
                ((AbstractElderCard)card).isElderNumberModified = v;
            }
        }

        @Override
        public String key() { //controls what card text will be recognized as the magic number
            return KitsuneMod.makeID("Elder");
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            if (card instanceof AbstractElderCard) {
                return ((AbstractElderCard)card).upgradedElderNumber;
            } else {
                return false;
            }
        }

        @Override
        public int value(AbstractCard card) {
            if (card instanceof AbstractElderCard) {
                return ((AbstractElderCard)card).elderNumber;
            } else {
                return -1;
            }
        }

        @Override
        public Color getDecreasedValueColor() {
            return Settings.GREEN_TEXT_COLOR;
        }

        @Override
        public Color getIncreasedValueColor() {
            return Settings.RED_TEXT_COLOR;
        }
    }
}
