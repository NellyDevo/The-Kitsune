package kitsunemod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractKitsuneCard extends CustomCard {
    //A card abstract is not required, but I like having one. Very convenient to set up things, such as custom dynamic variables.

    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;

    public AbstractKitsuneCard(String id, String name, String img, int cost, String rawDescription,
                               AbstractCard.CardType type, AbstractCard.CardColor color,
                               AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public void upgradeSecondMagicNumber(int amount) {
        baseSecondMagicNumber += amount;
        secondMagicNumber = baseSecondMagicNumber;
        upgradedSecondMagicNumber = true;
    }

    public static class SecondMagicNumber extends DynamicVariable { //custom dynamic variable class

        @Override
        public int baseValue(AbstractCard card) {
            if (card instanceof AbstractKitsuneCard) {
                return ((AbstractKitsuneCard)card).baseSecondMagicNumber;
            } else {
                return -1;
            }
        }

        @Override
        public boolean isModified(AbstractCard card) {
            if (card instanceof AbstractKitsuneCard) {
                return ((AbstractKitsuneCard)card).isSecondMagicNumberModified;
            } else {
                return false;
            }
        }

        @Override
        public void setIsModified(AbstractCard card, boolean v) {
            if (card instanceof AbstractKitsuneCard) {
                ((AbstractKitsuneCard)card).isSecondMagicNumberModified = v;
            }
        }

        @Override
        public String key() { //controls what card text will be recognized as the magic number
            return "kitsunemod:M2";
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            if (card instanceof AbstractKitsuneCard) {
                return ((AbstractKitsuneCard)card).upgradedSecondMagicNumber;
            } else {
                return false;
            }
        }

        @Override
        public int value(AbstractCard card) {
            if (card instanceof AbstractKitsuneCard) {
                return ((AbstractKitsuneCard)card).secondMagicNumber;
            } else {
                return -1;
            }
        }
    }
}
