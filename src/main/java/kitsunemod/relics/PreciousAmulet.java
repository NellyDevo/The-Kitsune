package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.powers.HumanShapePower;

public class PreciousAmulet extends KitsuneRelic implements ClickableRelic {
    public static final String ID = KitsuneMod.makeID("PreciousAmulet");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");
    public static final Texture ALT_IMG = new Texture("kitsunemod/images/relics/starterrelic_gray.png");

    public PreciousAmulet() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
        if (shouldStartInactive()) {
            counter = 0;
            img = ALT_IMG;
        } else {
            counter = 1;
        }
    }

    private boolean shouldStartInactive() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BrokenCollar.ID) && AbstractDungeon.player.getRelic(BrokenCollar.ID).counter == 1;
    }

    @Override
    public boolean shouldAutoChangeShape() {
        if (counter == 1) {
            AbstractDungeon.actionManager.addToBottom(
                    new ChangeShapeAction(AbstractDungeon.player, AbstractDungeon.player, new HumanShapePower(AbstractDungeon.player, AbstractDungeon.player)));
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
        if (AbstractDungeon.player.hasRelic(BrokenCollar.ID)) {
            AbstractDungeon.player.getRelic(BrokenCollar.ID).counter = 0;
            AbstractDungeon.player.getRelic(BrokenCollar.ID).img = BrokenCollar.ALT_IMG;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PreciousAmulet();
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
