package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.powers.ShadePower;

public class QuicksandPouch extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("QuicksandPouch");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/quicksandpouch.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/quicksandpouch_p.png");

    private static final int SHADE_AMOUNT = 2;
    private static final int HP_THRESHOLD_PERCENTAGE = 50;

    public QuicksandPouch() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        if ((AbstractDungeon.player.currentHealth / AbstractDungeon.player.maxHealth * 100) < HP_THRESHOLD_PERCENTAGE) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ShadePower(AbstractDungeon.player, SHADE_AMOUNT), SHADE_AMOUNT));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_THRESHOLD_PERCENTAGE + DESCRIPTIONS[1] + SHADE_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new QuicksandPouch();
    }
}
