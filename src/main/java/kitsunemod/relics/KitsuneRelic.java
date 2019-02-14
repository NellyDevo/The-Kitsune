package kitsunemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;

import javax.xml.soap.Text;

public abstract class KitsuneRelic extends CustomRelic {

    public KitsuneRelic(String ID, Texture IMG, Texture OUTLINE, RelicTier TIER, LandingSound SOUND) {
        super(ID, IMG, OUTLINE, TIER, SOUND);
    }

    public void onChangeShape(KitsuneMod.KitsuneShapes shape) {}
}
