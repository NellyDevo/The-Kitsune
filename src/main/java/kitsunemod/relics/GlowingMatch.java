package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;

public class GlowingMatch extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("GlowingMatch");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/glowingmatch.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/glowingmatch_p.png");

    private static final int DAMAGE_INCREASE = 1;

    public GlowingMatch() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_INCREASE + DESCRIPTIONS[1];
    }

    @Override
    public int onCalculateWispDamage(int amount) {
        return amount + DAMAGE_INCREASE;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GlowingMatch();
    }
}
