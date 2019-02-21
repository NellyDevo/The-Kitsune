package kitsunemod.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChannelWillOWispAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.NinetailedShapePower;

public class DeceptiveGuise extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("DeceptiveGuise");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/default_skill.png";
    private static final int COST = 1;

    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;


    public DeceptiveGuise() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(HumanShapePower.POWER_ID) || p.hasPower(NinetailedShapePower.POWER_ID)) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDying && !monster.isDead && !monster.isEscaping) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
                }
            }
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DeceptiveGuise();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
