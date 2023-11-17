package kitsunemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.CreateWillOWispAction;
import kitsunemod.cards.basic.ChangeShape;
import kitsunemod.powers.AbstractShapePower;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.KitsuneShapePower;

public class ShiningPearl extends KitsuneRelic {
    public static final String ID = KitsuneMod.makeID("ShiningPearl");
    public static final Texture IMG = new Texture("kitsunemod/images/relics/shiningpearl.png");
    public static final Texture OUTLINE = new Texture("kitsunemod/images/relics/shiningpearl_p.png");

    public ShiningPearl() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    public static final int FOX_BONUS_STR = 0;
    public static final int FOX_BONUS_DEX = 2;
    public static final int KITSUNE_BONUS_STR = 1;
    public static final int KITSUNE_BONUS_DEX = 1;
    public static final int HUMAN_BONUS_STR = 2;
    public static final int HUMAN_BONUS_DEX = 0;
    public static final int KITSUNE_WILLOWISPS_AMOUNT = 1;

    @Override
    public void atPreBattle() {
        FoxShapePower.BONUS_STRENGTH = FOX_BONUS_STR;
        FoxShapePower.BONUS_DEXTERITY = FOX_BONUS_DEX;

        KitsuneShapePower.BONUS_STRENGTH = KITSUNE_BONUS_STR;
        KitsuneShapePower.BONUS_DEXTERITY = KITSUNE_BONUS_DEX;

        HumanShapePower.BONUS_STRENGTH = HUMAN_BONUS_STR;
        HumanShapePower.BONUS_DEXTERITY = HUMAN_BONUS_DEX;
    }

    @Override
    public void onVictory() {
        FoxShapePower.BONUS_STRENGTH = 0;
        FoxShapePower.BONUS_DEXTERITY = 0;

        KitsuneShapePower.BONUS_STRENGTH = 0;
        KitsuneShapePower.BONUS_DEXTERITY = 0;

        HumanShapePower.BONUS_STRENGTH = 0;
        HumanShapePower.BONUS_DEXTERITY = 0;
        description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(name,description));
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new MakeTempCardInHandAction(new ChangeShape()));
    }
    @Override
    public void atTurnStartPostDraw() {
        if (AbstractDungeon.player.hasPower(KitsuneShapePower.POWER_ID)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new CreateWillOWispAction(KITSUNE_WILLOWISPS_AMOUNT));
        }
    }

    @Override
    public void onChangeShape(KitsuneMod.KitsuneShapes shape, AbstractShapePower shapePower) {
        description = DESCRIPTIONS[0];

        //TODO handle ninetailed form; for that matter decide what we want to do with ninetailed form re: str/dex now
        if (shape == KitsuneMod.KitsuneShapes.FOX) {
            description += DESCRIPTIONS[1] + FOX_BONUS_STR + DESCRIPTIONS[4] + FOX_BONUS_DEX + DESCRIPTIONS[5];
        }
        if (shape == KitsuneMod.KitsuneShapes.KITSUNE) {
            description += DESCRIPTIONS[2] + KITSUNE_BONUS_STR + DESCRIPTIONS[4] + KITSUNE_BONUS_DEX + DESCRIPTIONS[5];
            if (KITSUNE_WILLOWISPS_AMOUNT == 1) {
                description += DESCRIPTIONS[6] + KITSUNE_WILLOWISPS_AMOUNT + DESCRIPTIONS[7];
            }
            else if (KITSUNE_WILLOWISPS_AMOUNT > 1) {
                description += DESCRIPTIONS[6] + KITSUNE_WILLOWISPS_AMOUNT + DESCRIPTIONS[8];
            }
        }
        if (shape == KitsuneMod.KitsuneShapes.HUMAN) {
            description += DESCRIPTIONS[3] + FOX_BONUS_STR + DESCRIPTIONS[4] + FOX_BONUS_DEX + DESCRIPTIONS[5];
        }

        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.initializeTips();
    }
    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(WornPearl.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(WornPearl.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(WornPearl.ID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShiningPearl();
    }
}
