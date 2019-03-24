package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ApplyLightAction;

public class HumanShapePower extends AbstractShapePower {

    //set in WornPearl, LuminousPearl, and ShiningPearl if you're looking for the base strength/dex amounts
    public static int BONUS_DEXTERITY = 0;
    public static int BONUS_STRENGTH = 0;
    public static int STACKS_PER_SOULSTEAL = 2;

    public static final String POWER_ID = KitsuneMod.makeID("HumanShapePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";


    public HumanShapePower(final AbstractCreature owner, final AbstractCreature source) {
        super(owner, source, DESCRIPTIONS, KitsuneMod.KitsuneShapes.HUMAN, BONUS_STRENGTH, BONUS_DEXTERITY);

        name = NAME;
        ID = POWER_ID;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/HumanShapePower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/HumanShapePower_32.png"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public AbstractGameAction getSoulstealActionForAmount(AbstractPlayer player, int amount) {
        return new ApplyLightAction(player, player, amount * STACKS_PER_SOULSTEAL);
    }

    @Override
    public String getSoulstealUIString(int amount) {
        return DESCRIPTIONS[12] + amount * STACKS_PER_SOULSTEAL + DESCRIPTIONS[13] + DESCRIPTIONS[8] + STACKS_PER_SOULSTEAL + DESCRIPTIONS[9] + DESCRIPTIONS[11];
    }
}
