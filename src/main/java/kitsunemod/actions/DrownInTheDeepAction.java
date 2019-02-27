package kitsunemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class DrownInTheDeepAction extends AbstractGameAction {
    private AbstractMonster target;
    private AbstractPlayer p;
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private int damage;
    private boolean initialized = false;
    private int effect;
    private float duration = 0.1f;

    public DrownInTheDeepAction(AbstractMonster target, boolean isThisFreeToPlayOnce, int energyOnUse, int damage) {
        freeToPlayOnce = isThisFreeToPlayOnce;
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
        this.target = target;
        this.energyOnUse = energyOnUse;
        this.damage = damage;
    }

    @Override
    public void update() {
        if (!initialized) {
            p = AbstractDungeon.player;
            effect = EnergyPanel.totalCount;
            if (energyOnUse != -1) {
                effect = energyOnUse;
            }
            if (p.hasRelic(ChemicalX.ID)) {
                effect += 2;
                p.getRelic(ChemicalX.ID).flash();
            }
            initialized = true;
        }
        if (duration == 0.1f) {
            if (effect > 0) {
                target.damage(new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.HP_LOSS));
                damage *= 2;
                --effect;
            } else {
                isDone = true;
            }
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration <= 0.0f) {
            duration = 0.1f;
        }
        if (isDone) {
            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
    }
}
