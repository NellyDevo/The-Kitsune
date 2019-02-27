package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;

public class CuriousVial extends KitsuneRelic implements ClickableRelic {
    public static final String ID = KitsuneMod.makeID("BrokenCollar");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    private static final int MAX_STACKS = 10;

    public CuriousVial() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
        counter = 0;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && counter < MAX_STACKS) {
            ++counter;
        }
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && counter > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(AbstractDungeon.player, AbstractDungeon.player, counter));
            counter = 0;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MAX_STACKS + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CuriousVial();
    }
}
