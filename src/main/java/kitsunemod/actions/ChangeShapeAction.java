package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.*;

public class ChangeShapeAction extends AbstractGameAction {
    private AbstractCreature target;
    private KitsuneMod.KitsuneShapes newShape;

    public ChangeShapeAction(
            final AbstractCreature target,
            final AbstractCreature source,
            final KitsuneMod.KitsuneShapes newShape)

    {
        this.source = source;
        this.target = target;
        this.newShape = newShape;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (target.hasPower(getPowerIDForShape(newShape))) {
            isDone = true;
            return;
        }
        if (target.hasPower(NinetailedShapePower.POWER_ID)) {
            //ninetailed is all three forms so skip out and return
            isDone = true;
            return;
        }

        if (target.hasPower(getPowerIDForShape(KitsuneMod.KitsuneShapes.FOX))) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, getPowerIDForShape(KitsuneMod.KitsuneShapes.FOX)));
        }
        if (target.hasPower(getPowerIDForShape(KitsuneMod.KitsuneShapes.KITSUNE))) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, getPowerIDForShape(KitsuneMod.KitsuneShapes.KITSUNE)));
        }
        if (target.hasPower(getPowerIDForShape(KitsuneMod.KitsuneShapes.HUMAN))) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, getPowerIDForShape(KitsuneMod.KitsuneShapes.HUMAN)));
        }
        switch (newShape) {
            case FOX:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new FoxShapePower(target, source)));
                break;
            case KITSUNE:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new KitsuneShapePower(target, source)));
                break;
            case HUMAN:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new HumanShapePower(target, source)));
                break;
            case NINETAILED:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new NinetailedShapePower(target, source)));
        }
        isDone = true;
    }

    //maybe this should be somewhere else but for now its staying here
    private String getPowerIDForShape(final KitsuneMod.KitsuneShapes shape) {
        switch (shape) {
            case FOX:
                return FoxShapePower.POWER_ID;
            case KITSUNE:
                return KitsuneShapePower.POWER_ID;
            case HUMAN:
                return HumanShapePower.POWER_ID;
            case NINETAILED:
                //TODO fix this when we have a Ninetails shape power and add here
                return NinetailedShapePower.POWER_ID;
            default:
                return "ShouldntBeHere";
        }
    }

}
