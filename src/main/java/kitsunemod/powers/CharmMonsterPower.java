package kitsunemod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import kitsunemod.KitsuneMod;

import java.lang.reflect.Field;

public class CharmMonsterPower extends AbstractPower {
    public static final String POWER_ID = KitsuneMod.makeID("Charmed");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private byte moveByte;
    private AbstractMonster.Intent moveIntent;

    public EnemyMoveInfo move;
    public Actions actions;
    public Color storedColor;
    public Field intentColorField;

    public CharmMonsterPower(AbstractMonster owner, int amount) {
        this.amount = amount;
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        isTurnBased = true;
        updateDescription();
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/charmed_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/powers/charmed_32.png"), 0, 0, 32, 32);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfRound() {
        if (amount > 1) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
        } else {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    try {
                        intentColorField.set(owner, storedColor);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void onInitialApplication() {
        // Dumb action to delay grabbing monster's intent until after it's actually set
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update()
            {
                if (owner instanceof AbstractMonster) {
                    moveByte = ((AbstractMonster) owner).nextMove;
                    moveIntent = ((AbstractMonster) owner).intent;
                    try {

                        intentColorField = AbstractMonster.class.getDeclaredField("intentColor");
                        intentColorField.setAccessible(true);
                        storedColor = (Color) intentColorField.get(owner);

                        Field moveField = AbstractMonster.class.getDeclaredField("move");
                        moveField.setAccessible(true);
                        move = (EnemyMoveInfo) moveField.get(owner);

                        boolean otherMonsterExists = false;
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (monster != owner && !monster.isDeadOrEscaped() ) {
                                otherMonsterExists = true;
                            }
                        }

                        switch (moveIntent) {

                            case ATTACK:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        if (otherMonsters) {
                                            DamageInfo info = new DamageInfo(owner, moveInfo.baseDamage);
                                            info.applyPowers(owner, target);
                                            // deal damage
                                            if (moveInfo.isMultiDamage) {
                                                for (int i = 0; i < moveInfo.multiplier; ++i) {
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                            }
                                        }
                                    };
                                    intentColorField.set(owner, Color.GREEN.cpy());
                                } else {
                                    //stunned
                                    actions = (moveInfo, target, otherMonsters) -> flash();
                                    move.intent = AbstractMonster.Intent.STUN;
                                }
                                break;

                            case ATTACK_BUFF:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        if (otherMonsters) {
                                            DamageInfo info = new DamageInfo(owner, moveInfo.baseDamage);
                                            info.applyPowers(owner, target);
                                            //deal damage
                                            if (moveInfo.isMultiDamage) {
                                                for (int i = 0; i < moveInfo.multiplier; ++i) {
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                            }
                                        }
                                        //weak buff player
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, 1), 1));
                                    };
                                } else {
                                    //weak buff player
                                    actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, 1), 1));
                                    move.intent = AbstractMonster.Intent.BUFF;
                                }
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case ATTACK_DEBUFF:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        if (otherMonsters) {
                                            DamageInfo info = new DamageInfo(owner, moveInfo.baseDamage);
                                            info.applyPowers(owner, target);
                                            //deal damage
                                            if (moveInfo.isMultiDamage) {
                                                for (int i = 0; i < moveInfo.multiplier; ++i) {
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                            }
                                            //weak debuff enemy
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new WeakPower(target, 2, false/*TODO*/), 2));
                                        }
                                    };
                                    intentColorField.set(owner, Color.GREEN.cpy());
                                } else {
                                    //stunned
                                    actions = (moveInfo, target, otherMonsters) -> flash();
                                    move.intent = AbstractMonster.Intent.STUN;
                                }
                                break;

                            case ATTACK_DEFEND:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        if (otherMonsters) {
                                            DamageInfo info = new DamageInfo(owner, moveInfo.baseDamage);
                                            info.applyPowers(owner, target);
                                            //deal damage
                                            if (moveInfo.isMultiDamage) {
                                                for (int i = 0; i < moveInfo.multiplier; ++i) {
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                                            }
                                        }
                                        //weak defend player
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, 8)));
                                    };
                                } else {
                                    //weak defend player
                                    actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, 8)));
                                    move.intent = AbstractMonster.Intent.DEFEND;
                                }
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case BUFF:
                                //normal buff player
                                actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, 2), 2));
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEBUFF:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        //normal debuff random enemy
                                        if (otherMonsters) {
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new WeakPower(target, 2, false/*TODO*/), 2));
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new VulnerablePower(target, 2, false/*TODO*/), 2));
                                        }
                                    };
                                    intentColorField.set(owner, Color.GREEN.cpy());
                                } else {
                                    //stunned
                                    actions = (moveInfo, target, otherMonsters) -> flash();
                                    move.intent = AbstractMonster.Intent.STUN;
                                }
                                break;

                            case STRONG_DEBUFF:
                                actions = (moveInfo, target, otherMonsters) -> {
                                    //debuff all enemies
                                    for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, owner, new WeakPower(monster, 2, false/*TODO*/), 2));
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, owner, new VulnerablePower(monster, 2, false/*TODO*/), 2));
                                    }
                                };
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEFEND:
                                //normal defend player
                                actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, 15)));
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEFEND_DEBUFF:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        //weak debuff random enemy
                                        if (otherMonsters) {
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new WeakPower(target, 2, false/*TODO*/), 2));
                                        }
                                        //weak defend player
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, 8)));
                                    };
                                } else {
                                    //weak defend player
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, 8)));
                                    move.intent = AbstractMonster.Intent.DEFEND;
                                }
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEFEND_BUFF:
                                actions = (moveInfo, target, otherMonsters) -> {
                                    //weak buff player
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, 1), 1));
                                    //weak defend player
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, 8)));
                                };
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            default:
                                //stunned
                                actions = (moveInfo, target, otherMonsters) -> flash();
                                move.intent = AbstractMonster.Intent.STUN;
                                break;
                        }
                        ((AbstractMonster) owner).createIntent();
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                isDone = true;
            }
        });
    }

    @Override
    public void onRemove() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)owner;
            if (move != null) {
                m.setMove(moveByte, moveIntent, move.baseDamage, move.multiplier, move.isMultiDamage);
            } else {
                m.setMove(moveByte, moveIntent);
            }
            m.createIntent();
            m.applyPowers();
        }
    }

    public interface Actions {
        void doActions(EnemyMoveInfo moveInfo, AbstractMonster target, boolean otherMonsters);
    }
}
