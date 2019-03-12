package kitsunemod.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import kitsunemod.orbs.WillOWisp;
import kitsunemod.vfx.WillOWispProjectile;

import java.util.ArrayList;

public class WillOWispAction extends AbstractGameAction {
    public WillOWispProjectile child;
    private ArrayList<WillOWispProjectile> children;
    private boolean initialized = false;
    private boolean isParent = false;
    private DamageInfo info;
    public WillOWisp orb;
    private boolean restoreSlot;

    public WillOWispAction(float x, float y, AbstractCreature target, DamageInfo info, float duration, Color startColor, Color endColor, WillOWisp orb, int imgIndex, float glowScale, boolean restoreSlot) {
        child = new WillOWispProjectile(x, y, target, duration, startColor, endColor, imgIndex, glowScale);
        AbstractDungeon.effectList.add(child);
        this.info = info;
        this.orb = orb;
        this.restoreSlot = restoreSlot;
    }

    @Override
    public void update() {
        if (!initialized) {
            isParent = true;
            AbstractPlayer p = AbstractDungeon.player;
            children = new ArrayList<>();
            children.add(child);
            AbstractDungeon.player.orbs.remove(orb);
            if (restoreSlot) {
                if (orb.tookSlot) {
                    p.orbs.add(new EmptyOrbSlot());
                } else {
                    --p.maxOrbs;
                }
            }
            float frameExtender = 0.0f;
            for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
                if (action instanceof WillOWispAction) {
                    ((WillOWispAction)action).initialized = true;
                    frameExtender += Gdx.graphics.getDeltaTime();
                    ((WillOWispAction)action).child.endDuration += frameExtender;
                    children.add(((WillOWispAction)action).child);
                    WillOWisp otherOrb = ((WillOWispAction)action).orb;
                    AbstractDungeon.player.orbs.remove(otherOrb);
                    if (restoreSlot) {
                        if (otherOrb.tookSlot) {
                            p.orbs.add(new EmptyOrbSlot());
                        } else {
                            --p.maxOrbs;
                        }
                    }
                }
            }
            for (int i = 0; i < p.orbs.size(); ++i) {
                p.orbs.get(i).setSlot(i, p.maxOrbs);
            }
            initialized = true;
        }
        isDone = true;
        if (isParent) {
            for (WillOWispProjectile projectile : children) {
                if (!projectile.didDamage) {
                    if (!projectile.target.isDeadOrEscaped()) {
                        if (projectile.doDamage && !projectile.didDamage) {
                            projectile.target.damageFlash = true;
                            projectile.target.damageFlashFrames = 4;
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
