package kitsunemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import kitsunemod.orbs.WillOWisp;
import kitsunemod.powers.WispAffectingPower;
import kitsunemod.relics.KitsuneRelic;

public class ChannelWillOWispAction extends AbstractGameAction {
    private static final int BASE_MAX_WISPS = 10;
    private static int MAX_WISPS;

    public ChannelWillOWispAction(int amount) {
        this.amount = amount;
        duration = Settings.ACTION_DUR_FAST;
        MAX_WISPS = calculateMaxWisps(BASE_MAX_WISPS);
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer p = AbstractDungeon.player;
            int a = amount;
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof WispAffectingPower) {
                    a = ((WispAffectingPower)power).onChannelWisp(a);
                }
            }
            while(a > 0) {
                int wispCount = 0;
                for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                    if (orb instanceof WillOWisp) {
                        wispCount++;
                    }
                }
                boolean hasEmptySlot = false;
                if(wispCount < MAX_WISPS) {
                    if (p.maxOrbs < 10) {
                        for (AbstractOrb orb : p.orbs) {
                            if (orb instanceof EmptyOrbSlot) {
                                hasEmptySlot = true;
                                break;
                            }
                        }
                        if (hasEmptySlot) {
                            AbstractDungeon.player.increaseMaxOrbSlots(1, false);
                            p.channelOrb(new WillOWisp());
                            --a;
                        } else {
                            ++p.maxOrbs;
                            p.orbs.add(new WillOWisp());
                            for (int i = 0; i < p.orbs.size(); ++i) {
                                p.orbs.get(i).setSlot(i, p.maxOrbs);
                            }
                            --a;
                        }
                    } else {
                        int wispIndex = -1;
                        for (int i = 0; i < p.orbs.size(); ++i) {
                            if (p.orbs.get(i) instanceof EmptyOrbSlot) {
                                hasEmptySlot = true;
                            }
                            if (p.orbs.get(i) instanceof WillOWisp) {
                                if (wispIndex == -1) {
                                    wispIndex = i;
                                }
                            }
                        }
                        if (hasEmptySlot) {
                            p.channelOrb(new WillOWisp(true));
                        } else {
                            if (wispIndex != -1) {
                                WillOWisp oldWisp = (WillOWisp) p.orbs.get(wispIndex);
                                oldWisp.evokeWithoutAffectingSlots();
                                p.orbs.remove(oldWisp);
                                p.orbs.add(new WillOWisp(oldWisp.tookSlot));
                                for (int n = 0; n < p.orbs.size(); ++n) {
                                    p.orbs.get(n).setSlot(n, p.maxOrbs);
                                }
                            } else {
                                AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0f, WillOWisp.DESC[2], true));
                            }
                        }
                        --a;
                    }
                } else {
                    for (int i = 0; i < p.orbs.size(); ++i) {
                        if (p.orbs.get(i) instanceof WillOWisp) {
                            WillOWisp oldWisp = (WillOWisp) p.orbs.get(i);
                            oldWisp.evokeWithoutAffectingSlots();
                            p.orbs.remove(oldWisp);
                            p.orbs.add(new WillOWisp(oldWisp.tookSlot));
                            for (int n = 0; n < p.orbs.size(); ++n) {
                                p.orbs.get(n).setSlot(n, p.maxOrbs);
                            }
                            --a;
                            break;
                        }
                    }
                }
            }
        }
        tickDuration();
    }

    private static int calculateMaxWisps(int amount) {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof KitsuneRelic) {
                amount = ((KitsuneRelic)relic).onCalculateMaxWisps(amount);
            }
        }
        return amount;
    }
}
