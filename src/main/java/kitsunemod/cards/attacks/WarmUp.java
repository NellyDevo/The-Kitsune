package kitsunemod.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.FlashOfSteel;
import com.megacrit.cardcrawl.cards.colorless.SwiftStrike;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kitsunemod.KitsuneMod;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneTags;
import kitsunemod.powers.FoxShapePower;
import kitsunemod.powers.HumanShapePower;
import kitsunemod.powers.NinetailedShapePower;

public class WarmUp extends AbstractKitsuneCard {
    public static final String ID = KitsuneMod.makeID("WarmUp");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "kitsunemod/images/cards/WarmUp.png";
    private static final int COST = 1;

    private static final int ATTACK_DMG = 8;

    public WarmUp() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.KITSUNE_COLOR,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        String aspectAttackToGive = NourishingFlame.ID;
        if (p.hasPower(FoxShapePower.POWER_ID)) {
            aspectAttackToGive = BareFangs.ID;
        } else if (p.hasPower(HumanShapePower.POWER_ID)) {
            aspectAttackToGive = Flourish.ID;
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(CardLibrary.getCopy(NipAtHeels.ID)));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(CardLibrary.getCopy(aspectAttackToGive)));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(CardLibrary.getCopy(SwiftStrike.ID)));
        }
        
    }

    @Override
    public AbstractCard makeCopy() {
        return new WarmUp();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
