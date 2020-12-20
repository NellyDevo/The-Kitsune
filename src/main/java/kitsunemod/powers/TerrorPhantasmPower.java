package kitsunemod.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import kitsunemod.KitsuneMod;

public class TerrorPhantasmPower extends AbstractKitsunePower implements HealthBarRenderPower {

    public static final String POWER_ID = KitsuneMod.makeID("TerrorPhantasmPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Color barColor = Color.YELLOW.cpy();
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public TerrorPhantasmPower(AbstractCreature owner, int amount) {

        this.owner = owner;
        this.amount = amount;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.DEBUFF;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("curiosity");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(owner, AbstractDungeon.player, amount));
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return barColor;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
