package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import kitsunemod.KitsuneMod;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.nextRoom;

@SpirePatch(
        clz = EnergyPanel.class,
        method = "setEnergy"
)
public class EnergyPanelSetEnergyHookPatch {
    public EnergyPanelSetEnergyHookPatch() {
    }

    public static void Prefix(int energy) {
        if (energy != EnergyPanel.getCurrentEnergy()) {
            KitsuneMod.receiveEnergyChanged(energy - EnergyPanel.getCurrentEnergy());
        }
    }
}
