package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import kitsunemod.orbs.WillOWisp;

public class EmpoweredSliceAction extends AbstractGameAction {
    private DamageInfo primaryDamage;
    private DamageInfo secondaryDamage;
    private AttackEffect effect;

    public EmpoweredSliceAction(AbstractMonster m, DamageInfo primaryDamage, AttackEffect effect, DamageInfo secondaryDamage) {
        target = m;
        this.primaryDamage = primaryDamage;
        this.secondaryDamage = secondaryDamage;
        this.effect = effect;
    }

    public void update() {
        int wispCount = 0;
        int replaceCount = 0;
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractOrb orb : p.orbs) {
            if (orb instanceof WillOWisp) {
                ++wispCount;
                if (((WillOWisp)orb).tookSlot) {
                    ++replaceCount;
                }
            }
        }
        p.orbs.removeIf(orb -> orb instanceof WillOWisp);
        p.maxOrbs -= wispCount;
        p.increaseMaxOrbSlots(replaceCount,false);
        for (int i = 0; i < p.orbs.size(); ++i) {
            p.orbs.get(i).setSlot(i, p.maxOrbs);
        }
        if (wispCount > 0) {
            for (int i = 0; i < wispCount; ++i) {
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, secondaryDamage, AttackEffect.FIRE));
            }
        }
        AbstractDungeon.actionManager.addToTop(new DamageAction(target, primaryDamage, effect));
        isDone = true;
    }
}
