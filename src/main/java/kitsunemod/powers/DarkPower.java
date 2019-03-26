package kitsunemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import kitsunemod.KitsuneMod;
import kitsunemod.relics.KitsuneRelic;

public class DarkPower extends AbstractKitsunePower {

    public AbstractCreature source;
    public static final int TRIGGER_BASE_STACKS = 9;
    public static final float EFFECT_MULTIPLIER = 2f;

    public static final String POWER_ID = KitsuneMod.makeID("DarkPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final DamageInfo.DamageType DARK_DAMAGE_TYPE = DamageInfo.DamageType.THORNS;
    private int baseThreshold;

    public DarkPower(final AbstractCreature owner, final AbstractCreature source, final int stacks) {

        this.owner = owner;
        this.source = source;

        isTurnBased = false;
        name = NAME;
        ID = POWER_ID;
        type = PowerType.BUFF;
        amount = stacks;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/DarkPower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/DarkPower_32.png"), 0, 0, 32, 32);

        baseThreshold = TRIGGER_BASE_STACKS;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {

        if (amount >= calculateThreshold(baseThreshold)) {
            AbstractDungeon.effectList.add(new FlashPowerEffect(this));
            AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(source, (int)(amount * EFFECT_MULTIPLIER), DARK_DAMAGE_TYPE), AbstractGameAction.AttackEffect.POISON));
            KitsuneMod.receiveOnTriggerDark();
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

    }

    private int calculateThreshold(int amount) {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof KitsuneRelic) {
                amount = ((KitsuneRelic)relic).onCalculateDarkTriggerThreshold(amount);
            }
        }
        return amount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + EFFECT_MULTIPLIER + DESCRIPTIONS[1] + calculateThreshold(baseThreshold) + DESCRIPTIONS[2];
    }
}
