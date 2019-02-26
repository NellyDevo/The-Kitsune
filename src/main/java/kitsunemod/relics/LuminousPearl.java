package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.effects.ShowCardAndExhaustEffect;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;
import kitsunemod.powers.NinetailedShapePower;

public class LuminousPearl extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("LuminousPearl");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/starterrelic.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/starterrelic_p.png");

    public LuminousPearl() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }


    //so
    //starter relics never get onEquip called
    //for [curse word]ing no reason
    //this is a workaround while preserving /some/ readability, change the base values in the respective shapes please
    public static final int STR_PER_SHAPESHIFT_CARD = 1;
    public static final int DEX_PER_SHAPESHIFT_CARD = 1;

    @Override
    public boolean shouldAutoChangeShape() {
        int drawPileCards = exhaustShapeshiftCardsInPile(AbstractDungeon.player.drawPile, CardGroup.DRAW_PILE_X, CardGroup.DRAW_PILE_Y);
        int handCards = exhaustShapeshiftCardsInPile(AbstractDungeon.player.hand, CardGroup.DRAW_PILE_X, CardGroup.DRAW_PILE_Y);
        int discardPileCards = exhaustShapeshiftCardsInPile(AbstractDungeon.player.discardPile, CardGroup.DISCARD_PILE_X, CardGroup.DISCARD_PILE_Y);

        int strGain = (drawPileCards + handCards + discardPileCards) * STR_PER_SHAPESHIFT_CARD;
        int dexGain = (drawPileCards + handCards + discardPileCards) * DEX_PER_SHAPESHIFT_CARD;

        AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(AbstractDungeon.player, AbstractDungeon.player,
                new NinetailedShapePower(AbstractDungeon.player,AbstractDungeon.player,strGain,dexGain)));
        return false;
    }
    private int exhaustShapeshiftCardsInPile(CardGroup toCheck, float exhaustFromX, float exhaustFromY) {
        CardGroup shapeshiftCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (AbstractCard card : toCheck.group) {
            if (card.tags.contains(KitsuneTags.SHAPESHIFT_CARD)) {
                shapeshiftCards.addToTop(card);
            }
        }
        for (AbstractCard card : shapeshiftCards.group) {
            toCheck.removeCard(card);
            //afaict we have to duplicate notifying relics and powers of exhaust
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relic.onExhaust(card);
            }
            for (AbstractPower power : AbstractDungeon.player.powers) {
                power.onExhaust(card);
            }
            card.triggerOnExhaust();
            AbstractDungeon.player.exhaustPile.addToTop(card);
            AbstractDungeon.effectList.add(new ShowCardAndExhaustEffect(card, exhaustFromX, exhaustFromY));
        }
        return shapeshiftCards.size();

    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LuminousPearl();
    }
}
