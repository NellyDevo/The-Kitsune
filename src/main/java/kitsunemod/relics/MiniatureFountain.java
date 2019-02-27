package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyLightAction;

public class MiniatureFountain extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("MiniatureFountain");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    private static final int TURN_COUNT = 2;
    private static final int LIGHT_AMOUNT = 9;

    public MiniatureFountain() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
        counter = 0;
    }

    @Override
    public void atTurnStart() {
        ++counter;
        if (counter == TURN_COUNT) {
            beginLongPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (counter == TURN_COUNT) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(AbstractDungeon.player, AbstractDungeon.player, LIGHT_AMOUNT));
            stopPulse();
        }
    }

    @Override
    public void onVictory() {
        counter = -1;
        stopPulse();
    }

    @Override
    public void atBattleStart() {
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURN_COUNT + DESCRIPTIONS[1] + LIGHT_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MiniatureFountain();
    }
}
