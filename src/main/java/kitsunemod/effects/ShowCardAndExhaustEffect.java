package kitsunemod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

public class ShowCardAndExhaustEffect extends AbstractGameEffect {
    public static final float EFFECT_DURATION = 0.5f;
    private AbstractCard card;


    public ShowCardAndExhaustEffect(AbstractCard card, float cardSpawnX, float cardSpawnY) {
        this.card = card;
        UnlockTracker.markCardAsSeen(card.cardID);
        this.duration = EFFECT_DURATION;
        card.current_x = cardSpawnX;
        card.current_y = cardSpawnY;
        card.target_x = MathUtils.random((float)Settings.WIDTH * 0.35F, (float)Settings.WIDTH * 0.65F);
        card.target_y = MathUtils.random((float)Settings.HEIGHT * 0.1F, (float)Settings.HEIGHT * 0.4F);
        card.angle = card.targetAngle = 0f;
        card.drawScale = 0.2F;
        card.targetDrawScale = 0.6F;
        card.transparency = 0.01F;
        card.targetTransparency = 1.0F;
        card.fadingOut = false;
        if (AbstractDungeon.effectList.size() < 2) {
            CardCrawlGame.sound.play("CARD_DRAW_8");
        }
    }
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.card.update();
        if (this.duration < 0.0F) {
            AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(card));
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            this.card.render(sb);
        }

    }

    public void dispose() {
    }
}

