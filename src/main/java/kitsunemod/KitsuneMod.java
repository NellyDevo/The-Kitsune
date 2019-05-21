package kitsunemod;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import kitsunemod.cards.AbstractElderCard;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.cards.attacks.*;
import kitsunemod.cards.basic.*;
import kitsunemod.cards.powers.*;
import kitsunemod.cards.skills.*;
import kitsunemod.cards.special.QuickshapeFox;
import kitsunemod.cards.special.QuickshapeHuman;
import kitsunemod.cards.special.QuickshapeKitsune;
import kitsunemod.character.KitsuneCharacter;
import kitsunemod.wisps.WillOWisp;
import kitsunemod.patches.KitsuneEnum;
import kitsunemod.powers.*;
import kitsunemod.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static kitsunemod.patches.AbstractCardEnum.KITSUNE_COLOR;

@SpireInitializer
public class KitsuneMod implements
        EditCardsSubscriber,
        EditCharactersSubscriber,
        EditKeywordsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        PostInitializeSubscriber,
        PostBattleSubscriber,
        PostDrawSubscriber,
        PreMonsterTurnSubscriber,
        PreRoomRenderSubscriber,
        PostUpdateSubscriber {

    //                                      //
    // --- CONSTANTS AND REFERENCE DATA --- //
    //                                      //

    public static final Color kitsuneColor = CardHelper.getColor(245.0f, 145.0f, 65.0f); //change this to our class's decided color; currently leftover from mystic purple

    private static final String MOD_ID_PREFIX = "kitsunemod:";

    private static final String attackCard = "kitsunemod/images/512/bg_attack_kitsune.png";
    private static final String skillCard = "kitsunemod/images/512/bg_skill_kitsune.png";
    private static final String powerCard = "kitsunemod/images/512/bg_power_kitsune.png";
    private static final String energyOrb = "kitsunemod/images/512/card_kitsune_orb.png";
    private static final String attackCardPortrait = "kitsunemod/images/1024/bg_attack_kitsune.png";
    private static final String skillCardPortrait = "kitsunemod/images/1024/bg_skill_kitsune.png";
    private static final String powerCardPortrait = "kitsunemod/images/1024/bg_power_kitsune.png";
    private static final String energyOrbPortrait = "kitsunemod/images/1024/card_kitsune_orb.png";
    private static final String charButton = "kitsunemod/images/charSelect/button.png";
    private static final String charPortrait = "kitsunemod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "kitsunemod/images/manaSymbol.png";

    public static final Logger logger = LogManager.getLogger(KitsuneMod.class.getName());

    public static int shapeshiftsThisCombat = 0;
    public static int turnsSpentInSameShape = 1;

    public static int cardDrawsThisCombat = 0;
    public static int cardDrawsThisTurn = 0;

    public static ArrayList<WillOWisp> wisps = new ArrayList<>();

    public static String makeID(String id) {
        return MOD_ID_PREFIX + id;
    }

    //                                             //
    // --- INITIALIZATION AND CONTENT ADDITION --- //
    //                                             //

    public KitsuneMod(){
        BaseMod.subscribe(this);

        BaseMod.addColor(KITSUNE_COLOR,
                kitsuneColor, kitsuneColor, kitsuneColor, kitsuneColor, kitsuneColor, kitsuneColor, kitsuneColor,   //Background color, back color, frame color, frame outline color, description box color, glow color
                attackCard, skillCard, powerCard, energyOrb,                                                        //attack background image, skill background image, power background image, energy orb image
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,                        //as above, but for card inspect view
                miniManaSymbol);                                                                                    //appears in Mystic Purple cards where you type [E]
    }

    //Used by @SpireInitializer
    @SuppressWarnings("unused")
    public static void initialize(){
        KitsuneMod kitsuneMod = new KitsuneMod();
        logger.info("KitsuneMod successfully initialized!");
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeImg = new Texture("kitsunemod/images/badge.png");
        ModPanel settingsPanel = new ModPanel(); //we can add UI elements to this settings panel as desired
        BaseMod.registerModBadge(badgeImg, "The Kitsune Mod", "Jin the Fox, Johnny Devo", "Adds a new character to the game: The Kitsune.", settingsPanel);

        //load large sprite sheet for Will-O-Wisp
        if (WillOWisp.img == null) {
            WillOWisp.img = new TextureAtlas.AtlasRegion[72];
            int i = 0;
            for (int r = 0; r < 6; ++r) {
                for (int c = 0; c < 12; ++c) {
                    WillOWisp.img[i] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("kitsunemod/images/orbs/flame.png"), c * 85, r * 85, 85, 85);
                    ++i;
                }
            }
        }

        //add sounds
//        HashMap<String, Sfx> reflectedMap = (HashMap<String, Sfx>)ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
//        reflectedMap.put("kitsunemod:SPARKS", new Sfx("mysticmod/sounds/sparks.ogg"));
//        reflectedMap.put("kitsunemod:BOOK_RUNE_ONE", new Sfx("mysticmod/sounds/bookruneone.ogg"));
//        reflectedMap.put("kitsunemod:BOOK_RUNE_TWO", new Sfx("mysticmod/sounds/bookrunetwo.ogg"));
    }

    @Override
    public void receiveEditCards() {
        //dynamic variables
        BaseMod.addDynamicVariable(new AbstractKitsuneCard.SecondMagicNumber());
        BaseMod.addDynamicVariable(new AbstractElderCard.ElderNumber());

        //Basic
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Wink());
        BaseMod.addCard(new DancingLights());

        //Special
        BaseMod.addCard(new QuickshapeFox());
        BaseMod.addCard(new QuickshapeKitsune());
        BaseMod.addCard(new QuickshapeHuman());

        //Commons
        BaseMod.addCard(new FoxShape());
        BaseMod.addCard(new KitsuneShape());
        BaseMod.addCard(new HumanShape());
        BaseMod.addCard(new ThickFur());
        BaseMod.addCard(new NipAtHeels());
        BaseMod.addCard(new TestTheirTactics());
        BaseMod.addCard(new SplitSoul());
        BaseMod.addCard(new Radiance());
        BaseMod.addCard(new GrowingShadow());
        BaseMod.addCard(new TricksterFlame());
        BaseMod.addCard(new FeralInstinct());
        BaseMod.addCard(new InflictWounds());
        BaseMod.addCard(new AlluringGlimmer());
        BaseMod.addCard(new Insight());
        BaseMod.addCard(new FastShape());
        BaseMod.addCard(new LureCloser());
        BaseMod.addCard(new DeceptiveGuise());
        BaseMod.addCard(new EmpoweredSlice());

        //Uncommons
        BaseMod.addCard(new MemorizeSpell());
        BaseMod.addCard(new Ignite());
        BaseMod.addCard(new IgniteSilhouette());
        BaseMod.addCard(new EssenceTheft());
        BaseMod.addCard(new RendSpirit());
        BaseMod.addCard(new NourishingFlame());
        BaseMod.addCard(new BareFangs());
        BaseMod.addCard(new Flourish());
        BaseMod.addCard(new Cornered());
        BaseMod.addCard(new Puppeteer());
        BaseMod.addCard(new EtherealKnives());
        BaseMod.addCard(new TimidAppearance());
        BaseMod.addCard(new DominateWill());
        BaseMod.addCard(new PackKill());
        BaseMod.addCard(new HauntingLights());
        BaseMod.addCard(new FleshToStone());
        BaseMod.addCard(new FeedingFrenzy());
        BaseMod.addCard(new TransmuteSelf());
        BaseMod.addCard(new PurifyingBlaze());
        BaseMod.addCard(new RoaringFire());
        BaseMod.addCard(new PurityOfForm());
        BaseMod.addCard(new PleaForGuidance());
        BaseMod.addCard(new LieInWait());
        BaseMod.addCard(new UnstableShape());
        BaseMod.addCard(new SiphonSoul());
        BaseMod.addCard(new AncientMalice());
        BaseMod.addCard(new AncientWisdom());
        BaseMod.addCard(new ShiftIntoSpirit());
        BaseMod.addCard(new Serenity());
        BaseMod.addCard(new HeightenedReflexes());
        BaseMod.addCard(new WarmUp());
        BaseMod.addCard(new FaeProtection());
        BaseMod.addCard(new DivineSpirit());
        BaseMod.addCard(new PettyTheft());
        BaseMod.addCard(new HungryStrikes());

        //Rares
        BaseMod.addCard(new NinetailedForm());
        BaseMod.addCard(new LashOut());
        BaseMod.addCard(new VanishIntoShadows());
        BaseMod.addCard(new CleansingNova());
        BaseMod.addCard(new GatheringDark());
        BaseMod.addCard(new GatheringLight());
        BaseMod.addCard(new CommuneWithSpirits());
        BaseMod.addCard(new TerrorPhantasm());
        BaseMod.addCard(new AnsweredPrayers());
        BaseMod.addCard(new GrowingHunger());
        BaseMod.addCard(new ChangeShape());
        BaseMod.addCard(new PurityOfFunction());
        BaseMod.addCard(new WaningExistence());
        BaseMod.addCard(new DeathStroke());
        BaseMod.addCard(new BurnFromMemory());
        BaseMod.addCard(new BlinkSlash());
        BaseMod.addCard(new MassSuggestion());
        BaseMod.addCard(new DrownInTheDeep());
        BaseMod.addCard(new GlaresShadow());

        //Register cards that should be visible by default
        UnlockTracker.addCard(Defend.ID);
        UnlockTracker.addCard(Strike.ID);
        UnlockTracker.addCard(Wink.ID);
        UnlockTracker.addCard(DancingLights.ID);

        //testing purposes only, comment out for releases
        //BaseMod.addCard(new TestCard());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new KitsuneCharacter(CardCrawlGame.playerName), charButton, charPortrait, KitsuneEnum.KITSUNE_CLASS); //TODO: blue button
    }


    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String languageString = "kitsunemod/strings/" + getLanguageString(Settings.language);
        String keywordStrings = Gdx.files.internal(languageString + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = (Map)gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            // Keyword word = (Keyword)v;
            logger.info("Adding Keyword - " + v.NAMES[0]);
            String proper = v.NAMES[0].substring(0,1).toUpperCase() + v.NAMES[0].substring(1);
            BaseMod.addKeyword(MOD_ID_PREFIX, proper, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditStrings() {
        String languageString = "kitsunemod/strings/" + getLanguageString(Settings.language);
        String cardStrings = Gdx.files.internal(languageString + "/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String characterStrings = Gdx.files.internal(languageString + "/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
//        String potionStrings = Gdx.files.internal(languageString + "/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String powerStrings = Gdx.files.internal(languageString + "/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal(languageString + "/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
//        String runModStrings = Gdx.files.internal(languageString + "/run_mods.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(RunModStrings.class, runModStrings);
//        String uiStrings = Gdx.files.internal(languageString + "/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
//        String monsterStrings = Gdx.files.internal(languageString + "/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String eventStrings = Gdx.files.internal(languageString + "/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String orbStrings = Gdx.files.internal(languageString + "/orbs.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(OrbStrings.class, orbStrings);
    }

    private String getLanguageString(Settings.GameLanguage language) { //for future localization support
        switch (language) {
//            case ZHS:
//                return "zhs";
            default:
                return "eng";
        }
    }

    @Override
    public void receiveEditRelics() {
        //starter
        BaseMod.addRelicToCustomPool(new WornPearl(), KITSUNE_COLOR);
        BaseMod.addRelicToCustomPool(new ShiningPearl(), KITSUNE_COLOR);

        //special/event
        BaseMod.addRelicToCustomPool(new LuminousPearl(), KITSUNE_COLOR);

        //Common
        BaseMod.addRelicToCustomPool(new PreciousAmulet(), KITSUNE_COLOR);
        BaseMod.addRelicToCustomPool(new BrokenCollar(), KITSUNE_COLOR);
        BaseMod.addRelicToCustomPool(new MiniatureFountain(), KITSUNE_COLOR);

        //Uncommon
        BaseMod.addRelicToCustomPool(new GlowingMatch(), KITSUNE_COLOR);
        BaseMod.addRelicToCustomPool(new BlackAndWhiteStone(), KITSUNE_COLOR);
        BaseMod.addRelicToCustomPool(new CuriousVial(), KITSUNE_COLOR);

        //Rare
        BaseMod.addRelicToCustomPool(new PendulumOfEternalBalance(), KITSUNE_COLOR);
        BaseMod.addRelicToCustomPool(new QuicksandPouch(), KITSUNE_COLOR);

        //Shop

        //Boss
        BaseMod.addRelicToCustomPool(new FlameEyeNecklace(), KITSUNE_COLOR);
        BaseMod.addRelicToCustomPool(new RunicChoker(), KITSUNE_COLOR);
    }

    //                                  //
    // --- CODE HOOKS FOR MOD LOGIC --- //
    //                                  //

    @Override
    public void receivePostDraw(AbstractCard card) {
        cardDrawsThisCombat++;
        cardDrawsThisTurn++;
        boolean isExtraDraw = cardDrawsThisTurn > AbstractDungeon.player.gameHandSize;

        AbstractDungeon.player.drawPile.group.stream()
                .filter(c -> c instanceof AbstractElderCard)
                .forEach(c -> ((AbstractElderCard) c).onCardDrawn(card, isExtraDraw));

        AbstractDungeon.player.hand.group.stream()
                .filter(c -> c instanceof AbstractElderCard)
                .forEach(c -> ((AbstractElderCard) c).onCardDrawn(card, isExtraDraw));

        AbstractDungeon.player.discardPile.group.stream()
                .filter(c -> c instanceof AbstractElderCard)
                .forEach(c -> ((AbstractElderCard) c).onCardDrawn(card, isExtraDraw));

        AbstractDungeon.player.exhaustPile.group.stream()
                .filter(c -> c instanceof AbstractElderCard)
                .forEach(c -> ((AbstractElderCard) c).onCardDrawn(card, isExtraDraw));

        AbstractDungeon.player.limbo.group.stream()
                .filter(c -> c instanceof AbstractElderCard)
                .forEach(c -> ((AbstractElderCard) c).onCardDrawn(card, isExtraDraw));

        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard)AbstractDungeon.player.cardInUse).onCardDrawn(card, isExtraDraw);
        }
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster m) {
        cardDrawsThisTurn = 0;
        turnsSpentInSameShape++;
        if (m.hasPower(CharmMonsterPower.POWER_ID)) {
            CharmMonsterPower charm = (CharmMonsterPower)m.getPower(CharmMonsterPower.POWER_ID);
            boolean otherMonstersExist = false;
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster != m && !monster.isDeadOrEscaped()) {
                    otherMonstersExist = true;
                }
            }
            AbstractMonster target = null;
            if (otherMonstersExist) {
                target = AbstractDungeon.getRandomMonster(m);
            }
            charm.actions.doActions(charm.charmedMoveInfo, target, otherMonstersExist);
            charm.restoreMove();
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(m));
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(m, m, charm, 1));
            return false;
        }
        return true;
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        KitsuneMod.shapeshiftsThisCombat = 0;
        KitsuneMod.cardDrawsThisCombat = 0;
        KitsuneMod.cardDrawsThisTurn = 0;
        KitsuneMod.turnsSpentInSameShape = 1;
        AbstractDungeon.player.masterDeck.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onPostBattleOrRoomEntered());
    }

    public static void receiveOnMonsterDeath(AbstractMonster m) {
        AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onMonsterDied(m));

        AbstractDungeon.player.hand.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onMonsterDied(m));

        AbstractDungeon.player.discardPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onMonsterDied(m));

        AbstractDungeon.player.exhaustPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onMonsterDied(m));

        AbstractDungeon.player.limbo.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onMonsterDied(m));


        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard)AbstractDungeon.player.cardInUse).onMonsterDied(m);
        }
    }

    public static void receiveEnergyChanged(int energyDelta) {
        AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof AbstractKitsunePower)
                .forEach(power -> ((AbstractKitsunePower) power).onEnergyChanged(energyDelta));
    }

    public static void receiveRoomEntered(AbstractRoom room) {
        //technically misnamed now but resets upgradedThisRoom so that upgrades happen on the right cards
        AbstractDungeon.player.masterDeck.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onPostBattleOrRoomEntered());
        //the patch here is before an ? room is potentially rerolled - so i can assume that all ? rooms are instances of EventRoom
        AbstractDungeon.player.masterDeck.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onEnterRoom(room));

        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard)AbstractDungeon.player.cardInUse).onEnterRoom(room);
        }
    }

    public static void receiveChangeShape(KitsuneShapes shape, AbstractShapePower shapePower) {
        if (AbstractDungeon.player.relics != null) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                AbstractRelic r = AbstractDungeon.player.relics.get(i);
                if (r instanceof KitsuneRelic) {
                    ((KitsuneRelic) r).onChangeShape(shape, shapePower);
                }
            }
        }

        AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof AbstractKitsunePower)
                .forEach(power -> ((AbstractKitsunePower) power).onShapeChange(shape, shapePower));

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            monster.powers.stream()
                    .filter(power -> power instanceof AbstractKitsunePower)
                    .forEach(power -> ((AbstractKitsunePower) power).onShapeChange(shape, shapePower));
        }

        if (AbstractDungeon.player instanceof KitsuneCharacter) {
            ((KitsuneCharacter) AbstractDungeon.player).onShapeChange(shape);
        }

        KitsuneMod.shapeshiftsThisCombat++;
        KitsuneMod.turnsSpentInSameShape = 0;
    }

    public static int receivePlayerIsAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            int blockingAmount = Math.min(damageAmount, AbstractDungeon.player.currentBlock);

            if (blockingAmount > 0 && info.type != DamageInfo.DamageType.HP_LOSS) {
                AbstractDungeon.player.drawPile.group.stream()
                        .filter(card -> card instanceof AbstractElderCard)
                        .forEach(card -> ((AbstractElderCard) card).onBlockedDamage(blockingAmount));

                AbstractDungeon.player.hand.group.stream()
                        .filter(card -> card instanceof AbstractElderCard)
                        .forEach(card -> ((AbstractElderCard) card).onBlockedDamage(blockingAmount));

                AbstractDungeon.player.discardPile.group.stream()
                        .filter(card -> card instanceof AbstractElderCard)
                        .forEach(card -> ((AbstractElderCard) card).onBlockedDamage(blockingAmount));

                AbstractDungeon.player.exhaustPile.group.stream()
                        .filter(card -> card instanceof AbstractElderCard)
                        .forEach(card -> ((AbstractElderCard) card).onBlockedDamage(blockingAmount));

                AbstractDungeon.player.limbo.group.stream()
                        .filter(card -> card instanceof AbstractElderCard)
                        .forEach(card -> ((AbstractElderCard) card).onBlockedDamage(blockingAmount));

                if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
                    ((AbstractElderCard)AbstractDungeon.player.cardInUse).onBlockedDamage(blockingAmount);
                }
            }
        }
        return damageAmount;
    }
    public static void receivePlayerTookDamage(DamageInfo info,  int damageAmount) {
        //this function always gets triggered when its HP loss and when unblocked damage is > 0 post relics and powers so no sanity checking necessary
        AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onLoseHp(info, damageAmount));

        AbstractDungeon.player.hand.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onLoseHp(info, damageAmount));

        AbstractDungeon.player.discardPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onLoseHp(info, damageAmount));

        AbstractDungeon.player.exhaustPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onLoseHp(info, damageAmount));

        AbstractDungeon.player.limbo.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onLoseHp(info, damageAmount));

        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard) AbstractDungeon.player.cardInUse).onLoseHp(info, damageAmount);
        }
    }

    public static void receiveOnApplyDark(int amount) {
        AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyDark(amount));

        AbstractDungeon.player.hand.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyDark(amount));

        AbstractDungeon.player.discardPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyDark(amount));

        AbstractDungeon.player.exhaustPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyDark(amount));

        AbstractDungeon.player.limbo.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyDark(amount));

        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard)AbstractDungeon.player.cardInUse).onApplyDark(amount);
        }

        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof GatheringPower) {
                ((GatheringPower)p).onApplyLightOrDark(false);
            }
        }
    }

    public static void receiveOnApplyLight(int amount) {
        AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyLight(amount));

        AbstractDungeon.player.hand.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyLight(amount));

        AbstractDungeon.player.discardPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyLight(amount));

        AbstractDungeon.player.exhaustPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyLight(amount));

        AbstractDungeon.player.limbo.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onApplyLight(amount));

        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard)AbstractDungeon.player.cardInUse).onApplyLight(amount);
        }

        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof GatheringPower) {
                ((GatheringPower)p).onApplyLightOrDark(true);
            }
        }
    }

    public static void receiveOnTriggerLight() {
        AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerLight());

        AbstractDungeon.player.hand.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerLight());

        AbstractDungeon.player.discardPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerLight());

        AbstractDungeon.player.exhaustPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerLight());

        AbstractDungeon.player.limbo.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerLight());

        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard)AbstractDungeon.player.cardInUse).onTriggerLight();
        }
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof KitsuneRelic) {
                ((KitsuneRelic)relic).onTriggeredLight();
            }
        }
    }

    public static void receiveOnTriggerDark() {
        AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerDark());

        AbstractDungeon.player.hand.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerDark());

        AbstractDungeon.player.discardPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerDark());

        AbstractDungeon.player.exhaustPile.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerDark());

        AbstractDungeon.player.limbo.group.stream()
                .filter(card -> card instanceof AbstractElderCard)
                .forEach(card -> ((AbstractElderCard) card).onTriggerDark());

        if (AbstractDungeon.player.cardInUse instanceof AbstractElderCard) {
            ((AbstractElderCard)AbstractDungeon.player.cardInUse).onTriggerDark();
        }
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof KitsuneRelic) {
                ((KitsuneRelic)relic).onTriggeredDark();
            }
        }
    }

    @Override
    public void receivePreRoomRender(SpriteBatch sb) {
        if (!wisps.isEmpty() && AbstractDungeon.player != null && (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !AbstractDungeon.player.isDead) {
            for (WillOWisp wisp : wisps) {
                if (wisp.renderBehind) {
                    wisp.render(sb);
                }
            }
        }
    }

    @Override
    public void receivePostUpdate() {
        if (!wisps.isEmpty() && AbstractDungeon.player != null) {
            for (WillOWisp wisp : wisps) {
                wisp.update();
            }
        }
    }

    public static void calculateWispPositions() {
        if (!wisps.isEmpty()) {
            WillOWisp.calculateEllipseSize();

            //assign angles
            for (int i = 0; i < wisps.size(); ++i) {
                WillOWisp wisp = wisps.get(i);
                wisp.initialAngle = i * (360.0f / wisps.size());
                wisp.orbitalInterval = wisps.get(0).orbitalInterval;
            }
        }
    }

    public static void onTriggerEndOfPlayerTurnActions() {
        if (!wisps.isEmpty()) {
            boolean holdFire = false;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractKitsunePower) {
                    if (((AbstractKitsunePower)p).shouldHoldFire()) {
                        holdFire = true;
                        break;
                    }
                }
            }
            if (!holdFire) {
                for (WillOWisp wisp : wisps) {
                    wisp.onEndOfTurn();
                }
            }
        }
    }

    //
    //enums/interfaces
    //

    public enum KitsuneShapes {
        FOX,
        KITSUNE,
        HUMAN,
        NINETAILED
    }
}
