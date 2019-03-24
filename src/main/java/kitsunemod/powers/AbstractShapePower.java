package kitsunemod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kitsunemod.KitsuneMod;

public abstract class AbstractShapePower extends AbstractKitsunePower {

    public AbstractCreature source;

    private KitsuneMod.KitsuneShapes shape;
    private String[] descriptions;

    //yes, this is a duplicate of a private field on AbstractPower
    //why they aren't protected instead is beyond me
    private Color redColor = Color.RED.cpy();
    private Color greenColor = Color.GREEN.cpy();

    public AbstractShapePower(
            final AbstractCreature owner,
            final AbstractCreature source,
            final String[] descriptions,
            final KitsuneMod.KitsuneShapes shape,
            final int bonusStrength,
            final int bonusDexterity) {
        this.owner = owner;
        this.source = source;
        //I elected to put not!strength as the top number rather than shuffling around the description
        this.amount = bonusDexterity;
        this.amount2 = bonusStrength;
        this.shape = shape;
        this.descriptions = descriptions;

        isTurnBased = true;
        canGoNegative = true;
        type = PowerType.BUFF;
        priority = -300;

    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            if (damage + amount2 < 0.0f) {
                return 0.0f;
            }
            return damage + amount2;
        }
        return damage;
    }

    @Override
    public float modifyBlock(float block) {
        if (block + amount < 0.0f) {
            return 0.0f;
        }
        return block + amount;
    }

    public abstract AbstractGameAction getSoulstealActionForAmount(AbstractPlayer player, int amount);

    public abstract String getSoulstealUIString(int amount);

    @Override
    public void updateDescription() {
        if (amount == 0 && amount2 == 0) {
            description = descriptions[0];
        }
        else if (amount != 0 && amount2 == 0) {
            description = descriptions[1] + descriptions[3] + amount + descriptions[6];
        }
        else if (amount == 0 && amount2 != 0) {
            description = descriptions[1] + descriptions[2] + amount2 + descriptions[5];
        }
        else {
            description = descriptions[1] + descriptions[2] + amount2 + descriptions[4] + amount + descriptions[6];
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        //because TwoAmountPower calls Super and nothing actually modifies the color object we are passing, we can manipulate the render colors without copy paste
        if (amount2 < 0) {
            redColor.a = c.a;
            c = redColor;
        } else {
            greenColor.a = c.a;
            c = greenColor;
        }
        super.renderAmount(sb, x, y, c);
    }
}
