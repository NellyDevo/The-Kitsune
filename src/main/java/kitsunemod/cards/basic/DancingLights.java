package kitsunemod.cards.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChannelWillOWispAction;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;

public class DancingLights extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("DancingLights");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/DancingLights.png";

    private static final int COST = 2;

    private static final int ATTACK_DMG = 10;
    private static final int UPGRADE_PLUS_DMG = 5;

    private static final int WILLOWISPS_CHANNEL = 2;
    private static final int WILLOWISPS_PLUS_CHANNEL = 1;


    public DancingLights() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.BASIC, CardTarget.ENEMY);

        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = WILLOWISPS_CHANNEL;


    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ChannelWillOWispAction(magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DancingLights();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(WILLOWISPS_PLUS_CHANNEL);
        }
    }
}
