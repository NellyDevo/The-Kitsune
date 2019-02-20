package kitsunemod.powers;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;

public class TerrorPhantasmPower extends AbstractPower {

    public static final String POWER_ID = KitsuneMod.makeID("TerrorPhantasmPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
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
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
