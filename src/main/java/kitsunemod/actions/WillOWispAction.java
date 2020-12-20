package kitsunemod.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.vfx.WillOWispProjectile;
import kitsunemod.wisps.WillOWisp;

import java.util.ArrayList;

public class WillOWispAction extends AbstractGameAction {
    public WillOWispProjectile child;
    private ArrayList<WillOWispProjectile> children;
    private boolean initialized = false;
    private boolean isParent = false;
    private DamageInfo info;
    public WillOWisp wisp;

    public WillOWispAction(float x, float y, AbstractCreature target, DamageInfo info, float duration, Color startColor, Color endColor, WillOWisp wisp, int imgIndex, float glowScale) {
        child = new WillOWispProjectile(x, y, target, duration, startColor, endColor, imgIndex, glowScale);
        AbstractDungeon.effectList.add(child);
        this.info = info;
        this.wisp = wisp;
    }

    @Override
    public void update() {
        if (!initialized) {
            isParent = true;
            AbstractPlayer p = AbstractDungeon.player;
            children = new ArrayList<>();
            children.add(child);
            KitsuneMod.wisps.remove(wisp);
            float frameExtender = 0.0f;
            for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
                if (action instanceof WillOWispAction) {
                    ((WillOWispAction)action).initialized = true;
                    frameExtender += Gdx.graphics.getDeltaTime();
                    ((WillOWispAction)action).child.endDuration += frameExtender;
                    children.add(((WillOWispAction)action).child);
                    WillOWisp otherWisp = ((WillOWispAction)action).wisp;
                    KitsuneMod.wisps.remove(otherWisp);
                }
            }
            initialized = true;
        }
        isDone = true;
        if (isParent) {
            for (WillOWispProjectile projectile : children) {
                if (!projectile.didDamage) {
                    if (!projectile.target.isDeadOrEscaped()) {
                        if (projectile.doDamage && !projectile.didDamage) {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(projectile.target.hb.cX, projectile.target.hb.cY, AttackEffect.FIRE));
                            projectile.target.tint.color = Color.RED.cpy();
                            projectile.target.tint.changeColor(Color.WHITE.cpy());
                            projectile.target.damage(info);
                            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                                AbstractDungeon.actionManager.clearPostCombatActions();
                            }
                            projectile.didDamage = true;
                        }
                    } else {
                        AbstractMonster newTarget = AbstractDungeon.getRandomMonster();
                        if (newTarget != null) {
                            projectile.changeTarget(newTarget);
                        } else {
                            projectile.doDamage = true;
                            projectile.didDamage = true;
                        }
                    }
                }
            }
            for (WillOWispProjectile projectile : children) {
                if (!projectile.didDamage || !projectile.doDamage) {
                    isDone = false;
                    break;
                }
            }
        }
    }
}
