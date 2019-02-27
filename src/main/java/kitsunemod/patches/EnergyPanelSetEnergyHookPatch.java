package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import kitsunemod.KitsuneMod;

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
