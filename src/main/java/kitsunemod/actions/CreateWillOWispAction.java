package kitsunemod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kitsunemod.KitsuneMod;
import kitsunemod.wisps.WillOWisp;
import kitsunemod.powers.AbstractKitsunePower;
import kitsunemod.powers.WispAffectingPower;
import kitsunemod.relics.KitsuneRelic;

public class CreateWillOWispAction extends AbstractGameAction {
    private static final int BASE_MAX_WISPS = 9;
    private static int MAX_WISPS;

    public CreateWillOWispAction(int amount) {
        this.amount = amount;
        duration = Settings.ACTION_DUR_FAST;
        MAX_WISPS = calculateMaxWisps(BASE_MAX_WISPS);
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            int a = amount;
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof WispAffectingPower) {
                    a = ((WispAffectingPower)power).onChannelWisp(a);
                }
            }
            for (int i = 0; i < a; ++i) {
                if (KitsuneMod.wisps.size() < MAX_WISPS) {
                    KitsuneMod.wisps.add(new WillOWisp());
                    for (WillOWisp wisp : KitsuneMod.wisps) {
                        wisp.calculateDamage();
                    }
                } else {
                    WillOWisp extraWisp = new WillOWisp();
                    extraWisp.calculateDamage();
                    AbstractMonster target = AbstractDungeon.getRandomMonster();
                    AbstractDungeon.actionManager.addToTop(new WillOWispAction(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, target, new DamageInfo(AbstractDungeon.player, extraWisp.damage, DamageInfo.DamageType.THORNS), WillOWisp.PROJECTILE_FLIGHT_TIME, extraWisp.color, Color.RED.cpy(), extraWisp, extraWisp.imgIndex, extraWisp.glowScale));
                }
            }
            KitsuneMod.calculateWispPositions();
        }
        tickDuration();
    }

    private static int calculateMaxWisps(int amount) {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof KitsuneRelic) {
                amount = ((KitsuneRelic)relic).onCalculateMaxWisps(amount);
            }
        }
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof AbstractKitsunePower) {
                amount = ((AbstractKitsunePower)power).onCalculateMaxWisps(amount);
            }
        }
        return amount;
    }
}
