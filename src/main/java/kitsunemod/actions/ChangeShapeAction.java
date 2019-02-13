package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kitsunemod.KitsuneMod;
import kitsunemod.character.KitsuneCharacter;
import kitsunemod.powers.*;

public class ChangeShapeAction extends AbstractGameAction {
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
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
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
                    if (AbstractDungeon.player instanceof KitsuneCharacter) {
                        ((KitsuneCharacter)AbstractDungeon.player).transformToFox();
                    }
                    break;
                case KITSUNE:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new KitsuneShapePower(target, source)));
                    KitsuneMod.shapeshiftsThisCombat++;
                    if (AbstractDungeon.player instanceof KitsuneCharacter) {
                        ((KitsuneCharacter)AbstractDungeon.player).transformToKitsune();
                    }
                    break;
                case HUMAN:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new HumanShapePower(target, source)));
                    if (AbstractDungeon.player instanceof KitsuneCharacter) {
                        ((KitsuneCharacter)AbstractDungeon.player).transformToHuman();
                    }
                    break;
                case NINETAILED:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new NinetailedShapePower(target, source)));
            }
            KitsuneMod.shapeshiftsThisCombat++;
            KitsuneMod.turnsSpentInSameShape = 0;
        }
        tickDuration();
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
                return NinetailedShapePower.POWER_ID;
            default:
                return "ShouldntBeHere";
        }
    }

}
