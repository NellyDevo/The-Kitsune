package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import kitsunemod.cards.skills.MassSuggestion;
import kitsunemod.powers.CharmMonsterPower;

import java.util.ArrayList;

public class MassSuggestionAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private boolean upgraded;

    public MassSuggestionAction(boolean isThisFreeToPlayOnce, int energyOnUse, boolean upgraded) {
        freeToPlayOnce = isThisFreeToPlayOnce;
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (upgraded) {
            effect += 1;
        }
        if (effect > 0) {
            ArrayList<AbstractMonster> legalTargets = new ArrayList<>();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m.type != AbstractMonster.EnemyType.BOSS && !m.isDeadOrEscaped()) {
                    legalTargets.add(m);
                }
            }
            if (legalTargets.isEmpty()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f, MassSuggestion.EXTENDED_DESCRIPTION[0], true));
            } else {
                for (int i = 0; i < effect; ++i) {
                    AbstractMonster tmp = legalTargets.get(AbstractDungeon.cardRandomRng.random(legalTargets.size() - 1));
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(tmp, AbstractDungeon.player, new CharmMonsterPower(tmp, 1), 1));
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(tmp, AbstractDungeon.player, new VulnerablePower(tmp, 1, false), 1));
                }
            }
        }
        if (!freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
        isDone = true;
    }
}
