package kitsunemod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ApplyDarkAction;
import kitsunemod.actions.ApplyLightAction;
import kitsunemod.actions.ChannelWillOWispAction;
import kitsunemod.relics.KitsuneRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class SoulstealPower extends AbstractKitsunePower {
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
        assert target instanceof AbstractPlayer;

        AbstractPlayer targetPlayer = (AbstractPlayer) target;
        boolean shouldTrigger = checkCanSoulsteal(targetPlayer);
        if (shouldTrigger) {
            AbstractShapePower currentShape = getCurrentShapeForPlayer();
            if (currentShape != null) {
                AbstractGameAction actionToDo = currentShape.getSoulstealActionForAmount(targetPlayer, amount);
                AbstractDungeon.actionManager.addToBottom(actionToDo);
                hasSoulstealed = isFromAttack;
            } else {
                logger.info("Soulsteal attempted to apply to a player without a Shape, is this intentional?");
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        this.hasSoulstealed = false;
    }

    @Override
    public void updateDescription() {
        AbstractShapePower power = getCurrentShapeForPlayer();
        updateDescription(power);
    }

    private void updateDescription(AbstractShapePower playerShape) {
        description = DESCRIPTIONS[0];
        if (!checkCanSoulsteal(AbstractDungeon.player)) {
            description += DESCRIPTIONS[2];
        } else if (playerShape != null) {
            description += playerShape.getSoulstealUIString(amount);
        } else {
            description += DESCRIPTIONS[1];
        }

    }

    public void onShapeChange(KitsuneMod.KitsuneShapes shape, AbstractShapePower newPower) {
        updateDescription(newPower);
    }

    private boolean checkCanSoulsteal(AbstractPlayer player) {
        for (AbstractRelic relic : player.relics) {
            if (relic instanceof KitsuneRelic) {
                if (!((KitsuneRelic)relic).shouldTriggerSoulsteal()) {
                    return false;
                }
            }
        }
        return true;
    }

    private AbstractShapePower getCurrentShapeForPlayer() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.powers.size() == 0) {
            return null;
        }
        Optional<AbstractPower> shape = AbstractDungeon.player.powers.stream()
                .filter(power -> power.ID.startsWith(KitsuneMod.makeID("")) && power.ID.endsWith("ShapePower")).findFirst();
        if (shape.isPresent()) {
            AbstractPower result = shape.get();
            if (result instanceof AbstractShapePower) {
                KitsuneMod.logger.info("getCurrentShapeForPlayer: found " + result.ID);
                 return (AbstractShapePower)result;
            } else {
                KitsuneMod.logger.warn("Trying to find current shape for Soulsteal - found power matching \"kitsunemod:*ShapePower\" ID but it isn't an AbstractShapePower. It's a " + result.ID);
                return null;
            }
        }
        else
        {
            KitsuneMod.logger.info("getCurrentShapeForPlayer: found nothing");
            return null;
        }

    }
}
