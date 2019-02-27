package kitsunemod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BurnFromMemoryAction extends AbstractGameAction {
    private DamageInfo info;
    private int healAmount;
    private boolean firstFire = false;
    private boolean secondFire = false;
    private boolean thirdFire = false;

    public BurnFromMemoryAction(AbstractCreature target, DamageInfo info, int healAmount) {
        this.setValues(target, this.info = info);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.7f;
        this.healAmount = healAmount;
    }
    
    @Override
    public void update() {
        if (shouldCancelAction()) {
            isDone = true;
        }
        if (duration <= 0.7f && target != null && !firstFire) {
            firstFire = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX + MathUtils.random(-20.0f, 20.0f), target.hb.cY + MathUtils.random(-20.0f, 20.0f), AttackEffect.FIRE));
        }
        if (duration <= 0.4f && target != null && !secondFire) {
            secondFire = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX + MathUtils.random(-20.0f, 20.0f), target.hb.cY + MathUtils.random(-20.0f, 20.0f), AttackEffect.FIRE));
        }
        if (duration <= 0.1f && target != null && !thirdFire) {
            thirdFire = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX + MathUtils.random(-20.0f, 20.0f), target.hb.cY + MathUtils.random(-20.0f, 20.0f), AttackEffect.FIRE));
            target.damage(info);
            if ((((AbstractMonster)target).isDying || target.currentHealth <= 0) && AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth / 2) {
                AbstractDungeon.player.heal(healAmount);
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        this.tickDuration();
    }
}
