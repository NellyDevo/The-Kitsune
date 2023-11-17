package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.basic.ChangeShape;
import kitsunemod.powers.AbstractShapePower;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;

public class WornPearl extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("WornPearl");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/wornpearl.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/wornpearl_p.png");

    public WornPearl() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }


    @Override
    public void atBattleStart() {
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new MakeTempCardInHandAction(new ChangeShape()));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WornPearl();
    }
}
