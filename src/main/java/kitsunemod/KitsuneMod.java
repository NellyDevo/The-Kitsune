package kitsunemod;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import kitsunemod.cards.AbstractKitsuneCard;
import kitsunemod.cards.attacks.FoxShape;
import kitsunemod.cards.basic.DancingLights;
import kitsunemod.cards.basic.Defend;
import kitsunemod.cards.basic.Strike;
import kitsunemod.cards.TestCard;
import kitsunemod.cards.basic.Wink;
import kitsunemod.character.KitsuneCharacter;
import kitsunemod.orbs.WillOWisp;
import kitsunemod.patches.KitsuneEnum;
import kitsunemod.relics.StarterRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static kitsunemod.patches.AbstractCardEnum.KITSUNE_COLOR;

@SpireInitializer
public class KitsuneMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber {

    public static final Color kitsuneColor = CardHelper.getColor(152.0f, 34.0f, 171.0f); //change this to our class's decided color; currently leftover from mystic purple

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

    private static Logger logger = LogManager.getLogger(KitsuneMod.class.getName());


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
        //secondMagicNumber dynamic variable
        BaseMod.addDynamicVariable(new AbstractKitsuneCard.SecondMagicNumber()); //dynamic variable registered here

        //Basic
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Wink());
        BaseMod.addCard(new DancingLights());

        //Special

        //Commons
        BaseMod.addCard(new FoxShape());

        //Uncommons

        //Rares.

        //Register cards that should be visible by default
        UnlockTracker.addCard(Defend.ID);
        UnlockTracker.addCard(Strike.ID);
        UnlockTracker.addCard(Wink.ID);
        UnlockTracker.addCard(DancingLights.ID);

        //testing purposes only, comment out for releases
        BaseMod.addCard(new TestCard());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new KitsuneCharacter(CardCrawlGame.playerName), charButton, charPortrait, KitsuneEnum.KITSUNE_CLASS); //TODO: blue button
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
//        String eventStrings = Gdx.files.internal(languageString + "/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String orbStrings = Gdx.files.internal(languageString + "/orbs.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(OrbStrings.class, orbStrings);
    }

    @Override
    public void receiveEditRelics() {
        //starter
        BaseMod.addRelicToCustomPool(new StarterRelic(), KITSUNE_COLOR);

        //Common

        //Uncommon

        //Rare

        //Shop

        //Boss
    }
    public static String makeID(String id) {
        return MOD_ID_PREFIX + id;
    }

    public enum KitsuneShapes {
        FOX,
        KITSUNE,
        HUMAN,
        NINETAILED
    }
}
