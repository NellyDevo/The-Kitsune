package kitsunemod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.powers.*;

import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ApplyLightAction;
import kitsunemod.actions.ChannelWillOWispAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoulstealPower extends AbstractPower {
    public AbstractCreature source;
    private boolean hasSoulstealed = false;

    private static Logger logger = LogManager.getLogger(KitsuneMod.class.getName());

    public static final String POWER_ID = KitsuneMod.makeID("SoulstealPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    private static final int LIGHT_DARK_PER_STACK = 2;
    private static final int KITSUNE_WILL_O_WISPS = 1;
    private static final int NINETAILED_WILL_O_WISPS = 2;


    public SoulstealPower(final AbstractCreature owner, final AbstractCreature source, final int stacks) {

        this.owner = owner;
        this.source = source;

        name = NAME;
        ID = POWER_ID;
        type = PowerType.DEBUFF;
        isTurnBased = false;
        amount = stacks;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        updateDescription();
        loadRegion("constricted");

    }

    @Override
    public void onAttack(DamageInfo info, int amount, AbstractCreature target) {
        //this check really shouldn't fail, unless we make a card that applies Light or Dark to enemies for negative effects
        if (target instanceof AbstractPlayer && !hasSoulstealed) {
            applySoulsteal(target, true);
        }
    }

    public void applySoulsteal(AbstractCreature target, boolean isFromAttack) {
        AbstractPlayer targetPlayer = (AbstractPlayer)target;
        if (target.hasPower(FoxShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyDarkAction(targetPlayer, owner, this.amount * LIGHT_DARK_PER_STACK));
            hasSoulstealed = isFromAttack;
        }
        else if (target.hasPower(KitsuneShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChannelWillOWispAction(KITSUNE_WILL_O_WISPS));
            hasSoulstealed = isFromAttack;
        }
        else if (target.hasPower(HumanShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyLightAction(targetPlayer, owner, this.amount * LIGHT_DARK_PER_STACK));
            hasSoulstealed = isFromAttack;
        }
        else if (target.hasPower(NinetailedShapePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChannelWillOWispAction(NINETAILED_WILL_O_WISPS));
            hasSoulstealed = isFromAttack;
        }
        else {
            logger.info("Soulsteal attempted to apply to a player without a Shape, is this intentional?");
        }

    }

    @Override
    public void atStartOfTurn() {
        this.hasSoulstealed = false;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

    }


    @Override
    public void updateDescription() {

        if (amount == 1) {
            description = DESCRIPTIONS[0] + (amount * LIGHT_DARK_PER_STACK) + DESCRIPTIONS[1] + KITSUNE_WILL_O_WISPS + DESCRIPTIONS[2] + DESCRIPTIONS[4];
        }

        else if (amount > 1) {
            description = DESCRIPTIONS[0] + (amount * LIGHT_DARK_PER_STACK) + DESCRIPTIONS[1] + KITSUNE_WILL_O_WISPS + DESCRIPTIONS[3] + DESCRIPTIONS[4];
        }
    }

}
