package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import kitsunemod.KitsuneMod;

@SpirePatch(
        clz = EnergyPanel.class,
        method = "addEnergy"
)
public class EnergyPanelAddEnergyHookPatch {
    public EnergyPanelAddEnergyHookPatch() {
    }

    public static void Prefix(int e) {
        KitsuneMod.receiveEnergyChanged(e);
    }
}
