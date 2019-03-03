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

public class CharmMonsterPower extends AbstractKitsunePower {
    public static final String POWER_ID = KitsuneMod.makeID("Charmed");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int WEAK_BUFF_STR_AMT = 1;
    private static final int STRONG_BUFF_STR_AMT = 1;
    private static final int DEBUFF_WEAK_AMT = 2;
    private static final int DEBUFF_VULN_AMT = 2;
    private static final int WEAK_DEFEND_HEALTH_AMT = 8;
    private static final int DEFEND_HEALTH_AMT = 15;


    private byte oldMoveByte;
    private AbstractMonster.Intent oldMoveIntent;

    public EnemyMoveInfo charmedMoveInfo;
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
        AbstractMonster.Intent tooltipIntent = oldMoveIntent;
        int damage = -1;
        int attacks = -1;
        if (charmedMoveInfo != null) {
            tooltipIntent = charmedMoveInfo.intent;
            damage = charmedMoveInfo.baseDamage;
            attacks = charmedMoveInfo.multiplier;
        }
        //i am so sorry to whoever has to figure this out, especially if you don't have the json file
        //if you see this message i didn't get back to writing a small guide in comments and i pity you
        if (tooltipIntent != null) {
            switch(tooltipIntent) {
                case ATTACK:
                    description += DESCRIPTIONS[1] + damage;
                    if (attacks > 1) { description += "x" + attacks; }
                    description += DESCRIPTIONS[2];
                    break;
                case ATTACK_BUFF:
                    description += DESCRIPTIONS[1] + damage;
                    if (attacks > 1) { description += "x" + attacks; }
                    description += DESCRIPTIONS[12];
                    description += DESCRIPTIONS[13] + WEAK_BUFF_STR_AMT + DESCRIPTIONS[4];
                    break;
                case ATTACK_DEBUFF:
                    description += DESCRIPTIONS[1] + damage;
                    if (attacks > 1) { description += "x" + attacks; }
                    description += DESCRIPTIONS[12];
                    description += DESCRIPTIONS[14] + DEBUFF_WEAK_AMT + DESCRIPTIONS[6];
                    break;
                case ATTACK_DEFEND:
                    description += DESCRIPTIONS[1] + damage;
                    if (attacks > 1) { description += "x" + attacks; }
                    description += DESCRIPTIONS[15] + WEAK_DEFEND_HEALTH_AMT + DESCRIPTIONS[10];
                    break;
                case BUFF:
                    description += DESCRIPTIONS[3] + STRONG_BUFF_STR_AMT + DESCRIPTIONS[4];
                    break;
                case DEBUFF:
                    description += DESCRIPTIONS[5] + DEBUFF_WEAK_AMT + DESCRIPTIONS[6];
                    break;
                case STRONG_DEBUFF:
                    description += DESCRIPTIONS[5] + DEBUFF_WEAK_AMT + DESCRIPTIONS[7] + DEBUFF_VULN_AMT + DESCRIPTIONS[16];
                    break;
                case DEFEND:
                    description += DESCRIPTIONS[9] + DEFEND_HEALTH_AMT + DESCRIPTIONS[10];
                    break;
                case DEFEND_BUFF:
                    description += DESCRIPTIONS[9] + WEAK_DEFEND_HEALTH_AMT + DESCRIPTIONS[11];
                    description += DESCRIPTIONS[13] + WEAK_BUFF_STR_AMT + DESCRIPTIONS[4];
                    break;
                case DEFEND_DEBUFF:
                    description += DESCRIPTIONS[9] + WEAK_DEFEND_HEALTH_AMT + DESCRIPTIONS[11];
                    description += DESCRIPTIONS[14] + DEBUFF_WEAK_AMT + DESCRIPTIONS[6];
                    break;
                case STUN:
                default:
                    description += DESCRIPTIONS[17];
            }
        }
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
                    oldMoveByte = ((AbstractMonster) owner).nextMove;
                    oldMoveIntent = ((AbstractMonster) owner).intent;
                    try {

                        intentColorField = AbstractMonster.class.getDeclaredField("intentColor");
                        intentColorField.setAccessible(true);
                        storedColor = (Color) intentColorField.get(owner);

                        Field moveField = AbstractMonster.class.getDeclaredField("move");
                        moveField.setAccessible(true);
                        charmedMoveInfo = (EnemyMoveInfo) moveField.get(owner);

                        boolean otherMonsterExists = false;
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (monster != owner && !monster.isDeadOrEscaped() ) {
                                otherMonsterExists = true;
                            }
                        }

                        switch (oldMoveIntent) {

                            case ATTACK:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        if (otherMonsters) {
                                            DamageInfo info = new DamageInfo(owner, moveInfo.baseDamage);
                                            info.applyPowers(owner, target);
                                            // deal damage
                                            if (moveInfo.isMultiDamage) {
                                                for (int i = 0; i < moveInfo.multiplier; ++i) {
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                            }
                                        }
                                    };
                                    intentColorField.set(owner, Color.GREEN.cpy());
                                } else {
                                    //stunned
                                    actions = (moveInfo, target, otherMonsters) -> flash();
                                    charmedMoveInfo.intent = AbstractMonster.Intent.STUN;
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
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                            }
                                        }
                                        //weak buff player
                                        AbstractDungeon.actionManager.addToBottom(
                                                new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, WEAK_BUFF_STR_AMT), WEAK_BUFF_STR_AMT));
                                    };
                                } else {
                                    //weak buff player
                                    actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(
                                            new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, WEAK_BUFF_STR_AMT), WEAK_BUFF_STR_AMT));
                                    charmedMoveInfo.intent = AbstractMonster.Intent.BUFF;
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
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                            }
                                            //weak debuff enemy
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new WeakPower(target, DEBUFF_WEAK_AMT, true), DEBUFF_WEAK_AMT));
                                        }
                                    };
                                    intentColorField.set(owner, Color.GREEN.cpy());
                                } else {
                                    //stunned
                                    actions = (moveInfo, target, otherMonsters) -> flash();
                                    charmedMoveInfo.intent = AbstractMonster.Intent.STUN;
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
                                                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                                }
                                            } else {
                                                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AttackEffect.SMASH));
                                            }
                                        }
                                        //weak defend player
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, WEAK_DEFEND_HEALTH_AMT)));
                                    };
                                } else {
                                    //weak defend player
                                    actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, WEAK_DEFEND_HEALTH_AMT)));
                                    charmedMoveInfo.intent = AbstractMonster.Intent.DEFEND;
                                }
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case BUFF:
                                //normal buff player
                                actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, STRONG_BUFF_STR_AMT), STRONG_BUFF_STR_AMT));
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEBUFF:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        //normal debuff random enemy
                                        if (otherMonsters) {
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new WeakPower(target, DEBUFF_WEAK_AMT, true), DEBUFF_WEAK_AMT));
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new VulnerablePower(target, DEBUFF_VULN_AMT, true), DEBUFF_VULN_AMT));
                                        }
                                    };
                                    intentColorField.set(owner, Color.GREEN.cpy());
                                } else {
                                    //stunned
                                    actions = (moveInfo, target, otherMonsters) -> flash();
                                    charmedMoveInfo.intent = AbstractMonster.Intent.STUN;
                                }
                                break;

                            case STRONG_DEBUFF:
                                actions = (moveInfo, target, otherMonsters) -> {
                                    //debuff all enemies
                                    for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, owner, new WeakPower(monster, DEBUFF_WEAK_AMT, true), DEBUFF_WEAK_AMT));
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, owner, new VulnerablePower(monster, DEBUFF_VULN_AMT, true), DEBUFF_VULN_AMT));
                                    }
                                };
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEFEND:
                                //normal defend player
                                actions = (moveInfo, target, otherMonsters) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, DEFEND_HEALTH_AMT)));
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEFEND_DEBUFF:
                                if (otherMonsterExists) {
                                    actions = (moveInfo, target, otherMonsters) -> {
                                        //weak debuff random enemy
                                        if (otherMonsters) {
                                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, new WeakPower(target, DEBUFF_WEAK_AMT, true), DEBUFF_WEAK_AMT));
                                        }
                                        //weak defend player
                                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, WEAK_DEFEND_HEALTH_AMT)));
                                    };
                                } else {
                                    //weak defend player
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, WEAK_DEFEND_HEALTH_AMT)));
                                    charmedMoveInfo.intent = AbstractMonster.Intent.DEFEND;
                                }
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            case DEFEND_BUFF:
                                actions = (moveInfo, target, otherMonsters) -> {
                                    //weak buff player
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new StrengthPower(AbstractDungeon.player, WEAK_BUFF_STR_AMT), WEAK_BUFF_STR_AMT));
                                    //weak defend player
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new MonsterDefendPlayerPower(AbstractDungeon.player, owner, WEAK_DEFEND_HEALTH_AMT)));
                                };
                                intentColorField.set(owner, Color.GREEN.cpy());
                                break;

                            default:
                                //stunned
                                actions = (moveInfo, target, otherMonsters) -> flash();
                                charmedMoveInfo.intent = AbstractMonster.Intent.STUN;
                                break;
                        }
                        ((AbstractMonster) owner).createIntent();
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                ;
                updateDescription();
                isDone = true;
            }
        });
    }

    @Override
    public void onRemove() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)owner;
            if (charmedMoveInfo != null) {
                m.setMove(oldMoveByte, oldMoveIntent, charmedMoveInfo.baseDamage, charmedMoveInfo.multiplier, charmedMoveInfo.isMultiDamage);
            } else {
                m.setMove(oldMoveByte, oldMoveIntent);
            }
            m.createIntent();
            m.applyPowers();
        }
    }

    public interface Actions {
        void doActions(EnemyMoveInfo moveInfo, AbstractMonster target, boolean otherMonsters);
    }
}
