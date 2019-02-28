package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;

public class KitsuneShapePower extends AbstractShapePower {

    public AbstractCreature source;
    public static int BONUS_DEXTERITY = 0;
    public static int BONUS_STRENGTH = 0;

    public static final String POWER_ID = KitsuneMod.makeID("KitsuneShapePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";


    public KitsuneShapePower(final AbstractCreature owner, final AbstractCreature source) {
        super(owner, source, DESCRIPTIONS, KitsuneMod.KitsuneShapes.KITSUNE, BONUS_STRENGTH, BONUS_DEXTERITY);

        name = NAME;
        ID = POWER_ID;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/KitsuneShapePower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/KitsuneShapePower_32.png"), 0, 0, 32, 32);

        updateDescription();
    }
}
