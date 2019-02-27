package kitsunemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;

public class FoxShapePower extends AbstractShapePower {

    public AbstractCreature source;
    public static int BONUS_DEXTERITY = 0;
    public static int BONUS_STRENGTH = 0;

    public static final String POWER_ID = KitsuneMod.makeID("FoxShapePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";


    public FoxShapePower(final AbstractCreature owner, final AbstractCreature source) {
        super(owner, source, DESCRIPTIONS, KitsuneMod.KitsuneShapes.FOX, BONUS_STRENGTH, BONUS_DEXTERITY);

        name = NAME;
        ID = POWER_ID;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("demonForm");
        updateDescription();
    }
}
