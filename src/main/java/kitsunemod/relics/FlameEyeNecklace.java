package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;

public class FlameEyeNecklace extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("FlameEyeNecklace");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/flameeyenecklace.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/flameeyenecklace_p.png");

    private static final int MAX_WISP_REDUCE = 4;

    public FlameEyeNecklace() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MAX_WISP_REDUCE + DESCRIPTIONS[1];
    }

    @Override
    public int onCalculateMaxWisps(int amount) {
        return amount - MAX_WISP_REDUCE;
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FlameEyeNecklace();
    }
}
