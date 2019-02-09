package kitsunemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;

public class StarterRelic extends CustomRelic {
    public static final String ID = KitsuneMod.makeID("StarterRelic");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    public StarterRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

//    @Override
//    public void onUnequip() {
//        for (AbstractRelic relicInBossPool : RelicLibrary.bossList) {
//            if (relicInBossPool instanceof UGRADE_RELIC_CLASS()) {
//                RelicLibrary.bossList.remove(relicInBossPool);
//                break;
//            }
//        }
//    }

    @Override
    public AbstractRelic makeCopy() {
        return new StarterRelic();
    }
}
