package kitsunemod.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import kitsunemod.KitsuneMod;
import kitsunemod.relics.*;

public class ShrineOfBodyMindAndSoul extends AbstractImageEvent {
    public static final String ID = KitsuneMod.makeID("ShrineOfBodyMindAndSoul");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private int hpLoss;
    private int maxHpGain;
    private boolean finished = false;
    private boolean hasWornPearl;
    private AbstractPlayer p;

    public ShrineOfBodyMindAndSoul() {
        super(NAME, DESCRIPTIONS[0], "kitsunemod/images/events/ShrineOfBodyMindAndSoul.jpg");
        p = AbstractDungeon.player;
        hasWornPearl = p.hasRelic(WornPearl.ID);
        boolean hasShiningPearl = p.hasRelic(ShiningPearl.ID);
        boolean hasBrokenCollar = p.hasRelic(BrokenCollar.ID);
        boolean hasPreciousLocket = p.hasRelic(PreciousAmulet.ID);
        if (AbstractDungeon.ascensionLevel >= 15) {
            hpLoss = 5;
            maxHpGain = 3;
        } else {
            hpLoss = 4;
            maxHpGain = 4;
        }
        if ((hasWornPearl || hasShiningPearl) && hasBrokenCollar && hasPreciousLocket) {
            if (hasShiningPearl) {
                imageEventText.setDialogOption(OPTIONS[3]);
            } else {
                imageEventText.setDialogOption(OPTIONS[2]);
            }
        } else {
            if (hasShiningPearl) {
                imageEventText.setDialogOption(OPTIONS[1], true);
            } else {
                imageEventText.setDialogOption(OPTIONS[0], true);
            }
        }
        imageEventText.setDialogOption(OPTIONS[4] + hpLoss + OPTIONS[5] + maxHpGain + OPTIONS[6]);
        imageEventText.setDialogOption(OPTIONS[7]);
    }

    @Override
    public void buttonEffect(int i) {
        if (!finished) {
            switch (i) {
                case 0:
                    imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(OPTIONS[7]);
                    if (hasWornPearl) {
                        p.loseRelic(WornPearl.ID);
                    } else {
                        p.loseRelic(ShiningPearl.ID);
                    }
                    p.loseRelic(BrokenCollar.ID);
                    p.loseRelic(PreciousAmulet.ID);
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new LuminousPearl());
                    break;
                case 1:
                    imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(OPTIONS[7]);
                    p.increaseMaxHp(maxHpGain, true);
                    p.damage(new DamageInfo(null, hpLoss));
                    break;
                case 2:
                    imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(OPTIONS[7]);
                    break;
            }
            finished = true;
        } else {
            openMap();
        }
    }
}
