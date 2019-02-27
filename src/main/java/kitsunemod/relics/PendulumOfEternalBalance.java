package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ApplyLightAction;
import kitsunemod.powers.DarkPower;
import kitsunemod.powers.LightPower;

public class PendulumOfEternalBalance extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("PendulumOfEternalBalance");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    private static final int RESET_AMOUNT = 3;

    public PendulumOfEternalBalance() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onTriggeredDark() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, DarkPower.POWER_ID)); //interaction with mastery
        AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(AbstractDungeon.player, AbstractDungeon.player, RESET_AMOUNT));
    }

    @Override
    public void onTriggeredLight() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, LightPower.POWER_ID)); //interaction with mastery
        AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(AbstractDungeon.player, AbstractDungeon.player, RESET_AMOUNT));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + RESET_AMOUNT + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PendulumOfEternalBalance();
    }
}
