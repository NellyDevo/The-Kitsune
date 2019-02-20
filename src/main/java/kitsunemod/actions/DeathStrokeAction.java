package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DeathStrokeAction extends AbstractGameAction {
    private DamageInfo info;
    private int healAmount;
    private AbstractCard dupe;

    public DeathStrokeAction(AbstractCreature target, DamageInfo info, int healAmount, AbstractCard card) {
        this.setValues(target, this.info = info);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1f;
        this.healAmount = healAmount;
        dupe = card.makeSameInstanceOf();
        dupe.purgeOnUse = true;
    }
    
    @Override
    public void update() {
        if (duration == 0.1f && target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.SLASH_HEAVY));
            target.damage(info);
            if (((AbstractMonster)target).isDying || target.currentHealth <= 0) {
                AbstractDungeon.player.heal(healAmount);
                AbstractMonster newTarget = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
                if (newTarget != null) {
                    dupe.current_y = -200.0f * Settings.scale;
                    dupe.target_x = Settings.WIDTH / 2.0f + 200.0f * Settings.scale;
                    dupe.target_y = Settings.HEIGHT / 2.0f;
                    dupe.targetAngle = 0.0f;
                    dupe.lighten(false);
                    dupe.drawScale = 0.12f;
                    dupe.targetDrawScale = 0.75f;
                    dupe.freeToPlayOnce = true;
                    AbstractDungeon.actionManager.addToBottom(new QueueCardAction(dupe, newTarget));
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        this.tickDuration();
    }
}
