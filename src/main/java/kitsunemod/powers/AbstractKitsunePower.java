package kitsunemod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;

public abstract class AbstractKitsunePower extends TwoAmountPower implements GatheringPower, WispAffectingPower {

    public void onShapeChange(KitsuneMod.KitsuneShapes shape, AbstractShapePower power) {}

    public void onEnergyChanged(int e) {}

    @Override
    public boolean shouldConsume(AbstractPower p) {
        return true;
    }

    @Override
    public int onChannelWisp(int amount) {
        return amount;
    }

}
