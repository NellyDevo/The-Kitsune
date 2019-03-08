package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChannelWillOWispAction;

public class RoaringFirePower extends AbstractKitsunePower implements NonStackablePower {


    public static final String POWER_ID = KitsuneMod.makeID("RoaringFirePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public boolean isUpgraded = false;

    public RoaringFirePower(final AbstractCreature owner, int amount) {

        this.owner = owner;
        this.amount = amount;
        this.amount2 = 0;
        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/RoaringFirePower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/RoaringFirePower_32.png"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onEnergyChanged(int e) {
        if (e < 0) {
            int wispsToChannel = 0;
            amount2 += -e; //this function receives the the energy delta whenever it changes. therefore energy use will cause e to be negative therefore flip its sign
            if (amount2 >= amount) {
                float fAmount = (float)this.amount;
                float fCounter = (float)amount2;
                wispsToChannel = MathUtils.floor(fCounter / fAmount);
                amount2 -= (amount * wispsToChannel);
                AbstractDungeon.actionManager.addToBottom(new ChannelWillOWispAction(wispsToChannel));
            }
            //sanitizing negative inputs
            if (amount2 < 0) {
                amount2 = 0;
            }
        }
    }

    @Override
    public void updateDescription() {

        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
