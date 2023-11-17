package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import kitsunemod.powers.CharmMonsterPower;

public class KitsuneShapeAction extends AbstractGameAction {

    private AbstractPlayer source;
    private AbstractMonster target;
    private int amount;
    private DamageInfo damageInfo;

    public KitsuneShapeAction(
            final AbstractMonster target,
            final AbstractPlayer source,
            final int amount,
            final DamageInfo damageInfo)

    {
        this.source = source;
        this.target = target;
        this.damageInfo = damageInfo;
        this.amount = amount;
        actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_DIAGONAL));

            if (shouldCharm()) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new CharmMonsterPower(target, amount)));
            }

            target.damage(damageInfo);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        tickDuration();
    }

    private boolean shouldCharm() {
        int tempDamage = this.damageInfo.base;

        if (target.currentBlock > 0) {
            tempDamage -= target.currentBlock;
        }
        if (tempDamage > target.currentHealth) {
            tempDamage = target.currentHealth;
        }

        return (tempDamage > 0);
    }
}
