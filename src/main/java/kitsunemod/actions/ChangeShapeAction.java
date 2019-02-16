package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.*;

public class ChangeShapeAction extends AbstractGameAction {
    private AbstractShapePower newShape;
    private KitsuneMod.KitsuneShapes newShapeID;

    //i'm a shapeshifter
    //at poe's masquerade
    //hiding both face and mind
    //all free for you to draw

    public ChangeShapeAction(
            final AbstractCreature target,
            final AbstractCreature source,
            final AbstractShapePower newShape)

    {
        this.source = source;
        this.target = target;
        this.newShape = newShape;
        this.newShapeID = getShapeForPowerID(newShape);

        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST && !isDone) {
            if (target.hasPower(newShape.ID)) {
                isDone = true;
                return;
            }
            if (target.hasPower(NinetailedShapePower.POWER_ID)) {
                //ninetailed is all three forms so skip out and return
                isDone = true;
                return;
            }

            if (target.hasPower(FoxShapePower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, FoxShapePower.POWER_ID));
            }
            if (target.hasPower(KitsuneShapePower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, KitsuneShapePower.POWER_ID));
            }
            if (target.hasPower(HumanShapePower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, HumanShapePower.POWER_ID));
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, newShape));

            KitsuneMod.receiveChangeShape(newShapeID);
        }
        tickDuration();
    }

    private KitsuneMod.KitsuneShapes getShapeForPowerID(AbstractShapePower shape) {
        if (shape.ID.equals(FoxShapePower.POWER_ID)) {
            return KitsuneMod.KitsuneShapes.FOX;
        }
        if (shape.ID.equals(KitsuneShapePower.POWER_ID)) {
            return KitsuneMod.KitsuneShapes.KITSUNE;
        }
        if (shape.ID.equals(HumanShapePower.POWER_ID)) {
            return KitsuneMod.KitsuneShapes.HUMAN;
        }
        if (shape.ID.equals(NinetailedShapePower.POWER_ID)) {
            return KitsuneMod.KitsuneShapes.NINETAILED;
        }
        //shouldnt get here
        return KitsuneMod.KitsuneShapes.KITSUNE;
    }
}
