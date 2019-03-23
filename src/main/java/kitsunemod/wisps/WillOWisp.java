package kitsunemod.wisps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.WillOWispAction;
import kitsunemod.relics.KitsuneRelic;

public class WillOWisp {
    public Color color;
    public static TextureAtlas.AtlasRegion[] img;
    public int imgIndex;
    private float imgFrameRate = (1.0F / 30.0f);
    private float imgFrame = imgFrameRate;
    public float glowScale = 0.9f;
    private int glowTimer = 100;
    private int currGlowTimer = 0;
    private boolean glowBackswing = false;
    public boolean renderBehind = true;
    public float cX;
    public float cY;
    private float tX;
    private float tY;
    private int baseDamage;
    public int damage;
    private float fontScale = 1.0f;
    public float initialAngle;
    public float angle;
    public float orbitalInterval;

    private static float NUM_X_OFFSET = 20.0f * Settings.scale;
    private static float NUM_Y_OFFSET = -12.0f * Settings.scale;
    private static float ELLIPSIS_WIDTH = 100.0f * Settings.scale;
    private static float ELLIPSIS_HEIGHT = 75.0f * Settings.scale;
    private static float ORBIT_DURATION = 4.0f;
    public static float PROJECTILE_FLIGHT_TIME = 0.5f;
    private static float NON_ORBITAL_ADJUSTMENT_SPEED = 200.0f * Settings.scale;

    private static final float ELLIPSIS_MIN_WIDTH = 100.0f * Settings.scale;
    private static final float ELLIPSIS_MAX_WIDTH = 170.0f * Settings.scale;
    private static final float ELLIPSIS_MIN_HEIGHT = 75.0f * Settings.scale;
    private static final float ELLIPSIS_MAX_HEIGHT = 127.5f * Settings.scale;
    private static final float NON_ORBITAL_ADJUSTMENT_MIN_SPEED = 200.0f * Settings.scale;
    private static final float NON_ORBITAL_ADJUSTMENT_MAX_SPEED = 300.0f * Settings.scale;
    private static final int TARGET_WISP_COUNT_MAX_SIZE_ELLIPSE = 12;
    public static final int BASE_MAXIMUM_WISPS = 9;

    public WillOWisp() {
        if (img == null) {
            img = new TextureAtlas.AtlasRegion[72];
            int i = 0;
            for (int r = 0; r < 6; ++r) {
                for (int c = 0; c < 12; ++c) {
                    img[i] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/orbs/flame.png"), c * 85, r * 85, 85, 85);
                    ++i;
                }
            }
        }
        imgIndex = MathUtils.random(0, 71);
        baseDamage = 0;
        damage = this.baseDamage;
        color = new Color(0.0f, 1.0f, MathUtils.random(0.5f, 1.0f), 1.0f);
        cX = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_x;
        cY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_y + AbstractDungeon.player.hb_h / 2.0f;
    }

    public void calculateDamage() {
        int damage = baseDamage + KitsuneMod.wisps.size();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof KitsuneRelic) {
                damage = ((KitsuneRelic)relic).onCalculateWispDamage(damage);
            }
        }
        this.damage = damage;
    }

    public void onEndOfTurn() {
        calculateDamage();
        if (AbstractDungeon.getMonsters().monsters.stream().anyMatch(monster -> !monster.isDeadOrEscaped() && !monster.halfDead)) {
            AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
            AbstractDungeon.actionManager.addToBottom(new WillOWispAction(cX, cY, target, new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), PROJECTILE_FLIGHT_TIME, color, Color.RED.cpy(), this, imgIndex, glowScale));
        }
    }

    public void update() {
        calculateDamage();
        imgFrame -= Gdx.graphics.getDeltaTime();
        if (imgFrame <= 0) {
            ++imgIndex;
            imgFrame = imgFrameRate;
            if (!glowBackswing) {
                ++currGlowTimer;
                glowScale += 0.002;
                if (currGlowTimer == glowTimer) {
                    glowBackswing = true;
                }
            } else {
                --currGlowTimer;
                glowScale -= 0.002;
                if (currGlowTimer == 0) {
                    glowBackswing = false;
                }
            }
        }
        if (imgIndex > 71) {
            imgIndex = 0;
        }

        //calculate the angle given its current orbital duration
        angle = initialAngle + 360.0f * (orbitalInterval / ORBIT_DURATION);
        if (angle > 360.0f) {
            angle -= 360.0f;
        }

        //determine if the wisp should render behind the character
        renderBehind = angle < 180.0f;

        //based on Angle, find the target X coordinate
        float tmp = angle * ((float)Math.PI / 180.0f);
        tX = (ELLIPSIS_WIDTH * ELLIPSIS_HEIGHT) / (float)Math.sqrt((ELLIPSIS_HEIGHT * ELLIPSIS_HEIGHT) + ((ELLIPSIS_WIDTH * ELLIPSIS_WIDTH) * (Math.tan(tmp) * Math.tan(tmp))));
        if (90.0f < angle && angle < 270.0f) {
            tX *= -1;
        }

        //based on the target X coordinate, find the target Y coordinate
        tY = (float)Math.sqrt(((ELLIPSIS_WIDTH * ELLIPSIS_WIDTH * ELLIPSIS_HEIGHT * ELLIPSIS_HEIGHT) - (tX * tX * ELLIPSIS_HEIGHT * ELLIPSIS_HEIGHT)) / (ELLIPSIS_WIDTH * ELLIPSIS_WIDTH));
        if (180.0f < angle && angle < 360.0f) {
            tY *= -1;
        }

        //normalize target coordinates to player position
        tX += AbstractDungeon.player.hb.cX;
        tY += AbstractDungeon.player.hb.cY;

        //move towards target coordinates
        float distance = Vector2.dst(cX, cY, tX, tY);
        if (distance < NON_ORBITAL_ADJUSTMENT_SPEED * Gdx.graphics.getDeltaTime()) {
            cX = tX;
            cY = tY;
        } else {
            Vector2 startPoint = new Vector2(cX, cY);
            Vector2 endPoint = new Vector2(tX, tY);
            float direction = endPoint.sub(startPoint).angle();
            Vector2 movement = new Vector2(MathUtils.cosDeg(direction), MathUtils.sinDeg(direction));
            movement.x *= NON_ORBITAL_ADJUSTMENT_SPEED * Gdx.graphics.getDeltaTime();
            movement.y *= NON_ORBITAL_ADJUSTMENT_SPEED * Gdx.graphics.getDeltaTime();
            cX += movement.x;
            cY += movement.y;
        }

        //tick duration
        orbitalInterval += Gdx.graphics.getDeltaTime();
        if (orbitalInterval > ORBIT_DURATION) {
            orbitalInterval -= ORBIT_DURATION;
        }
    }

    public void render(final SpriteBatch sb) {
        sb.setColor(new Color(0.0f, 1.0f, 1.0f, 0.75f));
        sb.draw(img[imgIndex], cX - img[imgIndex].packedWidth / 2.0f, cY, img[imgIndex].packedWidth / 2.0f, img[imgIndex].packedHeight / 2.0f, img[imgIndex].packedWidth, img[imgIndex].packedHeight, 2.0f * Settings.scale * glowScale, 2.0f * Settings.scale * glowScale, 0.0f);
        sb.setColor(color);
        sb.draw(img[imgIndex], cX - img[imgIndex].packedWidth / 2.0f, cY, img[imgIndex].packedWidth / 2.0f, img[imgIndex].packedHeight / 2.0f, img[imgIndex].packedWidth, img[imgIndex].packedHeight, 2.0f * Settings.scale, 2.0f * Settings.scale, 0.0f);
        this.renderText(sb);
    }

    private void renderText(final SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.damage), this.cX + NUM_X_OFFSET, this.cY + NUM_Y_OFFSET - 4.0f * Settings.scale, new Color(0.2f, 1.0f, 1.0f, 1.0f), fontScale);
    }

    public static void calculateEllipseSize() {
        ELLIPSIS_WIDTH = Interpolation.linear.apply(ELLIPSIS_MIN_WIDTH, ELLIPSIS_MAX_WIDTH, (float)(Math.min(TARGET_WISP_COUNT_MAX_SIZE_ELLIPSE, KitsuneMod.wisps.size())) / (float)TARGET_WISP_COUNT_MAX_SIZE_ELLIPSE);
        ELLIPSIS_HEIGHT = Interpolation.linear.apply(ELLIPSIS_MIN_HEIGHT, ELLIPSIS_MAX_HEIGHT, (float)(Math.min(TARGET_WISP_COUNT_MAX_SIZE_ELLIPSE, KitsuneMod.wisps.size())) / (float)TARGET_WISP_COUNT_MAX_SIZE_ELLIPSE);
        NON_ORBITAL_ADJUSTMENT_SPEED = Interpolation.linear.apply(NON_ORBITAL_ADJUSTMENT_MIN_SPEED, NON_ORBITAL_ADJUSTMENT_MAX_SPEED, (float)(Math.min(TARGET_WISP_COUNT_MAX_SIZE_ELLIPSE, KitsuneMod.wisps.size())) / (float)TARGET_WISP_COUNT_MAX_SIZE_ELLIPSE);
    }
}
