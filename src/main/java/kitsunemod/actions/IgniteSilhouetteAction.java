package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class IgniteSilhouetteAction extends AbstractGameAction {

    private AbstractPlayer source;
    private AbstractMonster target;
    private DamageInfo damageInfo;

    public IgniteSilhouetteAction(
            final AbstractMonster target,
            final AbstractPlayer source,
            final DamageInfo damageInfo)

    {
        this.source = source;
        this.target = target;
        this.damageInfo = damageInfo;
        actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE));

            applyDark();

            target.damage(damageInfo);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        tickDuration();
    }

    private void applyDark() {
        int tempDamage = this.damageInfo.base;

        if (target.currentBlock > 0) {
            tempDamage -= target.currentBlock;
        }
        if (AbstractDungeon.player.hasRelic("Boot") && tempDamage < 5 && tempDamage > 0) {
            tempDamage = 5;
        }

        if (tempDamage > target.currentHealth) {
            tempDamage = target.currentHealth;
        }

        if (tempDamage > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(source, source, tempDamage));
        }
    }
}
