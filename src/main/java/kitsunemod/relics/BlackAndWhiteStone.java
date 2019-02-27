package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;

public class BlackAndWhiteStone extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("BlackAndWhiteStone");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    private static final int TRIGGER_DECREASE = 3;

    public BlackAndWhiteStone() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public int onCalculateDarkTriggerThreshold(int amount) {
        return amount - TRIGGER_DECREASE;
    }

    @Override
    public int onCalculateLightTriggerThreshold(int amount) {
        return amount - TRIGGER_DECREASE;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TRIGGER_DECREASE + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlackAndWhiteStone();
    }
}
