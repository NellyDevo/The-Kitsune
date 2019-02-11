package kitsunemod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class CleansingNovaAction extends AbstractGameAction {

    private AbstractPlayer source;

    public CleansingNovaAction(
            final AbstractPlayer source)

    {
        this.source = source;
        actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (source.currentBlock > 0) {
                int tempBlock = source.currentBlock;
                AbstractDungeon.effectList.add(new ShockWaveEffect(source.hb.cX, source.hb.cY, new Color(0.9f, 0.8f, 0.95f, 1.0f), ShockWaveEffect.ShockWaveType.CHAOTIC));

                int[] damageArray = new int[AbstractDungeon.monsterList.size()];

                for (int i = 0; i < damageArray.length; i++) {
                    damageArray[i] = tempBlock;
                }

                AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(source, damageArray, DamageInfo.DamageType.NORMAL, AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToTop(new SFXAction("MONSTER_AWAKENED_ATTACK"));
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.15f));
                AbstractDungeon.actionManager.addToTop(new RemoveAllBlockAction(source, source));

            }
            else {
                source.useShakeAnimation(0.5f);
                AbstractDungeon.actionManager.addToTop(new SFXAction("POWER_CONSTRICTED"));
            }
        }
        tickDuration();
    }
}
