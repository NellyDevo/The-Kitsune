package kitsunemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import kitsunemod.orbs.WillOWisp;

public class WillOWispProjectile extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion[] img = WillOWisp.img;
    public boolean doDamage = false;
    public boolean didDamage = false;
    public AbstractCreature target;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float x;
    private float y;
    public float endDuration;
    private Color startColor;
    private Color endColor;
    private Color color;
    private int imgIndex;
    private float imgFrameRate = (1.0F / 60.0f);
    private float imgFrame = imgFrameRate;
    private float glowScale;
    private float initialGlowScale;

    public WillOWispProjectile(float x, float y, AbstractCreature target, float endDuration, Color startColor, Color endColor, int imgIndex, float glowScale) {
        startX = this.x = x;
        startY = this.y = y;
        this.target = target;
        endX = target.hb.cX;
        endY = target.hb.cY;
        duration = 0.0f;
        this.endDuration = endDuration;
        this.startColor = startColor.cpy();
        this.endColor = endColor.cpy();
        color = startColor.cpy();
        this.imgIndex = imgIndex;
        this.glowScale = glowScale;
        initialGlowScale = glowScale;
    }

    @Override
    public void update() {
        //travelling position
        x = Interpolation.circleIn.apply(startX, endX, duration / endDuration);
        y = Interpolation.circleIn.apply(startY, endY, duration / endDuration);

        //transforming color
        color.r = Interpolation.linear.apply(startColor.r, endColor.r, duration / endDuration);
        color.g = Interpolation.linear.apply(startColor.g, endColor.g, duration / endDuration);
        color.b = Interpolation.linear.apply(startColor.b, endColor.b, duration / endDuration);

        //trail
        //AbstractDungeon.effectList.add(new WillOWispTrail(x, y, color.cpy()));

        //animation
        imgFrame -= Gdx.graphics.getDeltaTime();
        if (imgFrame <= 0) {
            ++imgIndex;
            imgFrame = imgFrameRate;
        }
        if (imgIndex > 71) {
            imgIndex = 0;
        }

        //fade glow
        glowScale = Interpolation.linear.apply(initialGlowScale, 0.0f, duration / (endDuration / 3));

        //update rotation
        Vector2 currentPosition = new Vector2(x, y);
        Vector2 targetPosition = new Vector2(endX, endY);
        float targetDirection = targetPosition.sub(currentPosition).angle() + 90.0f;
        if (targetDirection > 360.0f) {
            targetDirection -= 360.0f;
        }
        rotation = Interpolation.linear.apply(0.0f, targetDirection, duration / endDuration);

        //update duration
        if (duration >= endDuration) {
            doDamage = true;
            x = endX;
            y = endY;
        } else {
            duration += Gdx.graphics.getDeltaTime();
        }
        if (didDamage) {
            isDone = true;
        }
    }

    public void changeTarget(AbstractCreature newTarget) {
        float oldDistance = (float)Math.sqrt(Math.pow(startX - x, 2) + Math.pow(startY - y, 2));
        float newDistance = (float)Math.sqrt(Math.pow(newTarget.hb.cX - target.hb.cX, 2) + Math.pow(newTarget.hb.cY - target.hb.cY, 2));
        endDuration = endDuration * (newDistance / oldDistance);
        duration = 0.0f;
        startX = x;
        startY = y;
        target = newTarget;
        endX = target.hb.cX;
        endY = target.hb.cY;
        startColor = color;
        doDamage = false;
        didDamage = false;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (glowScale > 0) {
            sb.setColor(new Color(0.0f, 1.0f, 1.0f, 0.75f));
            sb.draw(img[imgIndex], x - img[imgIndex].packedWidth / 2.0f, y, img[imgIndex].packedWidth / 2.0f, img[imgIndex].packedHeight / 2.0f, img[imgIndex].packedWidth, img[imgIndex].packedHeight, 2.0f * Settings.scale * glowScale, 2.0f * Settings.scale * glowScale, rotation);
        }
        sb.setColor(color);
        sb.draw(img[imgIndex], x - img[imgIndex].packedWidth / 2.0f, y, img[imgIndex].packedWidth / 2.0f, img[imgIndex].packedHeight / 2.0f, img[imgIndex].packedWidth, img[imgIndex].packedHeight, 2.0f * Settings.scale, 2.0f * Settings.scale, rotation);
    }

    @Override
    public void dispose() {
    }
}
