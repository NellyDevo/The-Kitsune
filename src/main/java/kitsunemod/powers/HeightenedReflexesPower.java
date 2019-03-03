package kitsunemod.powers;

import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;

public class HeightenedReflexesPower extends AbstractKitsunePower {

    public boolean shouldUpgrade;
    private CardGroup zeroCosts;

    public static final String POWER_ID = KitsuneMod.makeID("HeightenedReflexesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    //public static final String IMG = "alternateVerseResources/images/powers/placeholder_power.png";

    public HeightenedReflexesPower(final AbstractCreature owner, boolean shouldUpgrade, int amount) {
        this.owner = owner;
        this.shouldUpgrade = shouldUpgrade;
        this.amount = amount;

        name = NAME;
        ID = POWER_ID;
        isTurnBased = false;
        canGoNegative = false;
        type = PowerType.BUFF;

        //temporary until I start making power art too
        //img = ImageMaster.loadImage(IMG);
        loadRegion("blur");
        updateDescription();
        zeroCosts = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : CardLibrary.getAllCards()) {
            if (card.cost == 0 &&
                    card.type == AbstractCard.CardType.ATTACK &&
                    (card.color == AbstractCard.CardColor.COLORLESS || card.color == AbstractCardEnum.KITSUNE_COLOR) &&
                    card.cardID != Shiv.ID) {
                zeroCosts.addToTop(card.makeCopy());
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != owner && info.type == DamageInfo.DamageType.NORMAL) {
            if (info.owner != null) {
                for (int i = 0; i < amount; i++) {
                    AbstractCard tempCard = zeroCosts.getRandomCard(AbstractDungeon.cardRandomRng).makeCopy();
                    if (shouldUpgrade) {
                        tempCard.upgrade();
                    }
                    tempCard.current_y = -200.0f * Settings.scale;
                    tempCard.target_x = Settings.WIDTH / 2.0f + 200.0f * Settings.scale;
                    tempCard.target_y = Settings.HEIGHT / 2.0f;
                    tempCard.targetAngle = 0.0f;
                    tempCard.lighten(false);
                    tempCard.drawScale = 0.12f;
                    tempCard.targetDrawScale = 0.75f;
                    tempCard.purgeOnUse = true;
                    tempCard.tags.add(KitsuneTags.IGNORES_TURN_RESTRICTION);
                    tempCard.dontTriggerOnUseCard = true;
                    AbstractDungeon.actionManager.addToTop(new QueueCardAction(tempCard, info.owner));
                }
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription() {

        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        if (shouldUpgrade) {
            description += DESCRIPTIONS[2];
        }
        if (amount == 1) {
            description += DESCRIPTIONS[3];
        }
        else {
            description += DESCRIPTIONS[4];
        }
    }
}
