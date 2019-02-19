package kitsunemod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import kitsunemod.KitsuneMod;

public abstract class AbstractShapePower extends AbstractPower {

    public AbstractCreature source;

    public int amount2 = 0;
    private KitsuneMod.KitsuneShapes shape;
    private String[] descriptions;

    //yes, this is a duplicate of a private field on AbstractPower
    //why they aren't protected instead is beyond me
    private Color redColor = Color.RED;
    private Color greenColor = Color.GREEN;

    public AbstractShapePower(
            final AbstractCreature owner,
            final AbstractCreature source,
            final String[] descriptions,
            final KitsuneMod.KitsuneShapes shape,
            final int bonusStrength,
            final int bonusDexterity) {
        this.owner = owner;
        this.source = source;
        this.amount = bonusStrength;
        this.amount2 = bonusDexterity;
        this.shape = shape;
        this.descriptions = descriptions;

        isTurnBased = false;
        canGoNegative = true;
        type = PowerType.BUFF;

    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            if (damage + amount < 0.0f) {
                return 0.0f;
            }
            return damage + (float)amount;
        }
        return damage;
    }

    @Override
    public float modifyBlock(float block) {
        if (block + amount2 < 0.0f) {
            return 0.0f;
        }
        return block + (float)amount2;
    }

    @Override
    public void updateDescription() {
        String strengthColorStr = "";
        String dexterityColorStr = "";
        if (amount > 0) {
            strengthColorStr = "#g";
        }
        else if (amount < 0) {
            strengthColorStr= "#r";
        }
        if (amount2 > 0) {
            dexterityColorStr = "#g";
        }
        else if (amount2 < 0) {
            dexterityColorStr = "#r";
        }
        if (amount == 0 && amount2 == 0) {
            description = descriptions[0];
        }
        else if (amount != 0 && amount2 == 0) {
            description = descriptions[1] + strengthColorStr + amount + descriptions[3];
        }
        else if (amount == 0 && amount2 != 0) {
            description = descriptions[1] + dexterityColorStr + amount2 + descriptions[4];
        }
        else {
            description = descriptions[1] + strengthColorStr + amount + descriptions[2] + dexterityColorStr + amount2 + descriptions[4];
        }
    }

    //also lifted this, again, same issue as above with the color constants. Frustrating that we have to copy code out of the base game like this
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.amount > 0) {
            if (!isTurnBased) {
                greenColor.a = c.a;
                c = greenColor;
            }

            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount), x, y, fontScale, c);
        } else if (this.amount < 0 && this.canGoNegative) {
            redColor.a = c.a;
            c = redColor;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount), x, y, fontScale, c);
        }
        if (amount2 > 0) {
            if (!isTurnBased) {
                greenColor.a = c.a;
                c = greenColor;
            }
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount2), x, y + 15.0F * Settings.scale, fontScale, c);
        }
        else if (amount2 < 0 && canGoNegative) {
            redColor.a = c.a;
            c = redColor;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount2), x, y + 15.0F * Settings.scale, fontScale, c);
        }
    }
}
