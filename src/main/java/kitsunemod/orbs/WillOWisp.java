package kitsunemod.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.WillOWispAction;

public class WillOWisp extends AbstractOrb {
    public static final String ORB_ID = KitsuneMod.makeID("Will-O-Wisp");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;
    public static final String NAME = orbString.NAME;
    private Color color;
    public boolean tookSlot = false;
    public static TextureAtlas.AtlasRegion[] img;
    private int imgIndex;
    private float imgFrameRate = (1.0F / 30.0f);
    private float imgFrame = imgFrameRate;
    private float glowScale = 0.9f;
    private int glowTimer = 100;
    private int currGlowTimer = 0;
    private boolean glowBackswing = false;

    public WillOWisp() {
        ID = ORB_ID;
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
        name = NAME;
        baseEvokeAmount = 0;
        evokeAmount = this.baseEvokeAmount;
        basePassiveAmount = 0;
        passiveAmount = this.basePassiveAmount;
        updateDescription();
        color = new Color(0.0f, 1.0f, MathUtils.random(0.5f, 1.0f), 1.0f);
        cX = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_x;
        cY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_y + AbstractDungeon.player.hb_h / 2.0f;
    }

    public WillOWisp(boolean tookSlot) {
        this();
        this.tookSlot = tookSlot;
    }

    @Override
    public void updateDescription() {
        description = DESC[0] + evokeAmount + DESC[1];
    }

    @Override
    public void applyFocus() {
        int damage = baseEvokeAmount;
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (orb instanceof WillOWisp) {
                ++damage;
            }
        }
        evokeAmount = damage;
        updateDescription();
    }

    @Override
    public void onEvoke() {
        applyFocus();
        AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        AbstractDungeon.actionManager.addToBottom(new WillOWispAction(cX, cY, target, new DamageInfo(AbstractDungeon.player, evokeAmount, DamageInfo.DamageType.THORNS), 0.33f, color, Color.RED.cpy(), this, imgIndex, glowScale));
        if (tookSlot) {
            for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                if (orb instanceof WillOWisp && !((WillOWisp)orb).tookSlot && orb != this) {
                    ((WillOWisp)orb).tookSlot = true;
                    tookSlot = false;
                    break;
                }
            }
        }
        if (tookSlot) {
            AbstractDungeon.actionManager.addToBottom(new EvokeOrbAction(1));
            AbstractDungeon.actionManager.addToBottom(new DecreaseMaxOrbAction(1));
        }
    }
    public void onEvoke(AbstractCreature target) {
        applyFocus();
        AbstractDungeon.actionManager.addToBottom(new WillOWispAction(cX, cY, target, new DamageInfo(AbstractDungeon.player, evokeAmount, DamageInfo.DamageType.THORNS), 0.33f, color, Color.RED.cpy(), this, imgIndex, glowScale));
        if (tookSlot) {
            for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                if (orb instanceof WillOWisp && !((WillOWisp)orb).tookSlot && orb != this) {
                    ((WillOWisp)orb).tookSlot = true;
                    tookSlot = false;
                    break;
                }
            }
        }
        if (tookSlot) {
            AbstractDungeon.actionManager.addToBottom(new EvokeOrbAction(1));
            AbstractDungeon.actionManager.addToBottom(new DecreaseMaxOrbAction(1));
        }

    }

    @Override
    public void onEndOfTurn() {
        applyFocus();
        AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        AbstractDungeon.actionManager.addToBottom(new WillOWispAction(cX, cY, target, new DamageInfo(AbstractDungeon.player, evokeAmount, DamageInfo.DamageType.THORNS), 0.5f, color, Color.RED.cpy(), this, imgIndex, glowScale));
    }

    @Override
    public void triggerEvokeAnimation() {

    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        applyFocus();
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

    }

    @Override
    public void render(final SpriteBatch sb) {
        sb.setColor(new Color(0.0f, 1.0f, 1.0f, 0.75f));
        sb.draw(img[imgIndex], cX - img[imgIndex].packedWidth / 2.0f, cY + this.bobEffect.y, img[imgIndex].packedWidth / 2.0f, img[imgIndex].packedHeight / 2.0f, img[imgIndex].packedWidth, img[imgIndex].packedHeight, 2.0f * Settings.scale * glowScale, 2.0f * Settings.scale * glowScale, 0.0f);
        sb.setColor(color);
        sb.draw(img[imgIndex], cX - img[imgIndex].packedWidth / 2.0f, cY + this.bobEffect.y, img[imgIndex].packedWidth / 2.0f, img[imgIndex].packedHeight / 2.0f, img[imgIndex].packedWidth, img[imgIndex].packedHeight, 2.0f * Settings.scale, 2.0f * Settings.scale, 0.0f);
        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    protected void renderText(final SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0f + NUM_Y_OFFSET - 4.0f * Settings.scale, new Color(0.2f, 1.0f, 1.0f, c.a), this.fontScale);
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ORB_PLASMA_CHANNEL", 0.1f); //TODO find channel sound
    }

    @Override
    public AbstractOrb makeCopy() {
        return new WillOWisp();
    }
}
