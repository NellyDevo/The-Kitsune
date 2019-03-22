package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.wisps.WillOWisp;

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
        AbstractPlayer p = AbstractDungeon.player;
        for (WillOWisp wisp : KitsuneMod.wisps) {
            ++wispCount;
        }
        KitsuneMod.wisps.clear();
        if (wispCount > 0) {
            for (int i = 0; i < wispCount; ++i) {
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, secondaryDamage, AttackEffect.FIRE));
            }
        }
        AbstractDungeon.actionManager.addToTop(new DamageAction(target, primaryDamage, effect));
        isDone = true;
    }
}
