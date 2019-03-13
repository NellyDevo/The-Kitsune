package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.powers.FoxShapePower;

public class BrokenCollar extends KitsuneRelic implements ClickableRelic {
    public static final String ID = KitsuneMod.makeID("BrokenCollar");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/brokencollar.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/brokencollar_p.png");
    public static final Texture ALT_IMG = new Texture("kitsunemod/images/relics/brokencollar_gray.png");

    public BrokenCollar() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
        if (shouldStartInactive()) {
            counter = 0;
            img = ALT_IMG;
        } else {
            counter = 1;
        }
    }

    private boolean shouldStartInactive() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(PreciousAmulet.ID) && AbstractDungeon.player.getRelic(PreciousAmulet.ID).counter == 1;
    }

    @Override
    public boolean shouldAutoChangeShape() {
        if (counter == 1) {
            AbstractDungeon.actionManager.addToBottom(
                    new ChangeShapeAction(AbstractDungeon.player, AbstractDungeon.player, new FoxShapePower(AbstractDungeon.player, AbstractDungeon.player)));
            flash();
            return false;
        }
        return true;
    }

    @Override
    public void onRightClick() {
        flash();
        counter = (counter == 0) ? 1 : 0;
        img = (counter == 0) ? ALT_IMG : IMG;
        if (counter == 1) {
            disableSimilarRelics();
        }
    }

    private void disableSimilarRelics() {
        if (AbstractDungeon.player.hasRelic(PreciousAmulet.ID)) {
            AbstractDungeon.player.getRelic(PreciousAmulet.ID).counter = 0;
            AbstractDungeon.player.getRelic(PreciousAmulet.ID).img = PreciousAmulet.ALT_IMG;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BrokenCollar();
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        //nop
    }

    @Override
    public void render(SpriteBatch sb) {
        if (counter == 1) {
            img = IMG;
        } else {
            img = ALT_IMG;
        }
        super.render(sb);
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        if (counter == 1) {
            img = IMG;
        } else {
            img = ALT_IMG;
        }
        super.renderInTopPanel(sb);
    }
}
