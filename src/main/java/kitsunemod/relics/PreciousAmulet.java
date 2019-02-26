package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.actions.ChannelWillOWispAction;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;

public class PreciousAmulet extends KitsuneRelic implements ClickableRelic {
    public static final String ID = KitsuneMod.makeID("PreciousAmulet");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    public boolean active = true;

    public PreciousAmulet() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
        active = true;
        counter = 1;

    }

    @Override
    public boolean shouldAutoChangeShape() {
        if (active) {
            AbstractDungeon.actionManager.addToBottom(
                    new ChangeShapeAction(AbstractDungeon.player, AbstractDungeon.player, new HumanShapePower(AbstractDungeon.player, AbstractDungeon.player)));
            beginLongPulse();
            return false;
        }
        return true;
    }

    @Override
    public void atPreBattle() {
    }

    @Override
    public void onVictory() {
        super.onVictory();
        if (active) {
            stopPulse();
        }
    }

    @Override
    public void onRightClick() {
        active = !active;
        flash();
        counter = (active) ? 1 : 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PreciousAmulet();
    }
}
