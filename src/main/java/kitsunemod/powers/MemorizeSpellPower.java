package kitsunemod.powers;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.powers.MemorizeSpell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MemorizeSpellPower extends AbstractPower implements NonStackablePower {

    private AbstractCard card;
    private boolean upgrade;

    public static final String POWER_ID = KitsuneMod.makeID("MemorizeSpellPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public MemorizeSpellPower(AbstractCreature owner, AbstractCard card, boolean isUpgraded) {
        this.owner = owner;
        this.card = card.makeCopy();
        upgrade = isUpgraded;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("corruption");
        updateDescription();

    }

    @Override
    public void atStartOfTurn() {
        AbstractCard newCard = card.makeCopy();
        if (upgrade) {
            newCard.upgrade();
        }
        newCard.exhaust = true;
        newCard.isEthereal = true;
        newCard.purgeOnUse = true;
        AlwaysRetainField.alwaysRetain.set(newCard, false);
        newCard.retain = false;
        newCard.name = MemorizeSpell.EXTENDED_DESCRIPTION[1] + newCard.name;
        try {
            Method initializeTitleMethod = AbstractCard.class.getDeclaredMethod("initializeTitle");
            initializeTitleMethod.invoke(newCard);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (((AbstractPlayer)owner).hand.size() == BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShowCardAndAddToDiscardEffect(card,Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F), 0.1f));
        } else {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShowCardAndAddToHandEffect(card,Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F), 0.1f));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (upgrade ? DESCRIPTIONS[2] : DESCRIPTIONS[1]) + card.name + DESCRIPTIONS[3];
    }
}
