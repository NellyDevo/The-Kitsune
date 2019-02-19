package kitsunemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import kitsunemod.KitsuneMod;

@SpirePatch(
        clz = EnergyPanel.class,
        method = "useEnergy"
)
public class EnergyPanelUseEnergyHookPatch {
    public EnergyPanelUseEnergyHookPatch() {
    }
    //inverting the value here because useEnergy gets passed a positive amount to consume, and our hook in KitsuneMod gets all changes
    public static void Prefix(int e) {
        KitsuneMod.receiveEnergyChanged(-e);
    }
}
