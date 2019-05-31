package kitsunemod.ui;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class EnergyOrbKitsune extends CustomEnergyOrb {
    private static Texture KITSUNE_ORB_LAYER_1_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer1.png");
    private static Texture KITSUNE_ORB_LAYER_1_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer1.png");
    private static Texture KITSUNE_ORB_LAYER_2_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer2.png");
    private static Texture KITSUNE_ORB_LAYER_2_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer2.png");
    private static Texture KITSUNE_ORB_LAYER_3_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer3.png");
    private static Texture KITSUNE_ORB_LAYER_3_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer3.png");
    private static Texture KITSUNE_ORB_LAYER_4_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer4.png");
    private static Texture KITSUNE_ORB_LAYER_4_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer4.png");
    private static Texture KITSUNE_ORB_LAYER_5_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer5.png");
    private static Texture KITSUNE_ORB_LAYER_5_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer5.png");
    private static Texture KITSUNE_ORB_LAYER_1D_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer1.png");
    private static Texture KITSUNE_ORB_LAYER_1D_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer1d.png");
    private static Texture KITSUNE_ORB_LAYER_2D_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer2d.png");
    private static Texture KITSUNE_ORB_LAYER_2D_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer2d.png");
    private static Texture KITSUNE_ORB_LAYER_3D_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer3d.png");
    private static Texture KITSUNE_ORB_LAYER_3D_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer3d.png");
    private static Texture KITSUNE_ORB_LAYER_4D_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer4d.png");
    private static Texture KITSUNE_ORB_LAYER_4D_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer4d.png");
    private static Texture KITSUNE_ORB_LAYER_5D_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/layer1d.png");
    private static Texture KITSUNE_ORB_LAYER_5D_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/layer1d.png");
    private static Texture KITSUNE_ORB_MASK_LEFT = ImageMaster.loadImage("kitsunemod/images/char/orb/orange/mask.png");
    private static Texture KITSUNE_ORB_MASK_RIGHT = ImageMaster.loadImage("kitsunemod/images/char/orb/purple/mask.png");
    private static Texture KITSUNE_ORB_LAYER_6 = ImageMaster.loadImage("kitsunemod/images/char/orb/combined/layer6.png");
    private static Texture KITSUNE_ORB_LAYER_7 = ImageMaster.loadImage("kitsunemod/images/char/orb/combined/layer7.png");
    private static float ORB_IMG_SCALE = 1.15f * Settings.scale;

    private FrameBuffer fbo1;
    private FrameBuffer fbo2;
    private float angle1Left;
    private float angle1Right;
    private float angle2Left;
    private float angle2Right;
    private float angle3Left;
    private float angle3Right;
    private float angle4Left;
    private float angle4Right;
    private float angle5Left;
    private float angle5Right;

    public EnergyOrbKitsune() {
        super(null, null, null);
        fbo1 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
        fbo2 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);

        orbVfx = ImageMaster.loadImage("kitsunemod/images/char/orb/vfx.png");
    }

    @Override
    public void updateOrb(int energyCount) {
        float time = Gdx.graphics.getDeltaTime();
        if (energyCount == 0) {
            angle5Left += time * -5.0f;
            angle4Left += time * 5.0f;
            angle3Left += time * -8.0f;
            angle2Left += time * 8.0f;
            angle1Left += time * 72.0f;
            angle5Right += time * 5.0f;
            angle4Right += time * -5.0f;
            angle3Right += time * 8.0f;
            angle2Right += time * -8.0f;
            angle1Right += time * -72.0f;
        } else {
            angle5Left += time * -20.0f;
            angle4Left += time * 20.0f;
            angle3Left += time * -40.0f;
            angle2Left += time * 40.0f;
            angle1Left += time * 360.0f;
            angle5Right += time * 20.0f;
            angle4Right += time * -20.0f;
            angle3Right += time * 40.0f;
            angle2Right += time * -40.0f;
            angle1Right += time * -360.0f;
        }
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        sb.end();

        //render left half in buffer
        fbo1.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true,true,true,true);
        sb.begin();
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (enabled) {
            sb.draw(KITSUNE_ORB_LAYER_1_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle1Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_2_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle2Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_3_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle3Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_4_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle4Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_5_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle5Left, 0, 0, 128, 128, false, false);
        } else {
            sb.draw(KITSUNE_ORB_LAYER_1D_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle1Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_2D_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle2Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_3D_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle3Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_4D_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle4Left, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_5D_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle5Left, 0, 0, 128, 128, false, false);
        }
        sb.setBlendFunction(0, GL20.GL_SRC_ALPHA);
        sb.setColor(new Color(1, 1, 1, 1));
        sb.draw(KITSUNE_ORB_MASK_LEFT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        fbo1.end();
        sb.end();
        TextureRegion leftHalf = new TextureRegion(fbo1.getColorBufferTexture());
        leftHalf.flip(false, true);

        //render right half in buffer
        fbo2.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true,true,true,true);
        sb.begin();
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (enabled) {
            sb.draw(KITSUNE_ORB_LAYER_1_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle1Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_2_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle2Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_3_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle3Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_4_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle4Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_5_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle5Right, 0, 0, 128, 128, false, false);
        } else {
            sb.draw(KITSUNE_ORB_LAYER_1D_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle1Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_2D_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle2Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_3D_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle3Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_4D_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle4Right, 0, 0, 128, 128, false, false);
            sb.draw(KITSUNE_ORB_LAYER_5D_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angle5Right, 0, 0, 128, 128, false, false);
        }
        sb.setBlendFunction(0, GL20.GL_SRC_ALPHA);
        sb.setColor(new Color(1,1,1,1));
        sb.draw(KITSUNE_ORB_MASK_RIGHT, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        fbo2.end();
        sb.end();
        TextureRegion rightHalf = new TextureRegion(fbo2.getColorBufferTexture());
        rightHalf.flip(false, true);

        //render whole
        sb.begin();
        sb.setColor(Color.WHITE);
        sb.draw(leftHalf, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
        sb.draw(rightHalf, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
        sb.draw(KITSUNE_ORB_LAYER_6, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        sb.draw(KITSUNE_ORB_LAYER_7, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
    }
}
