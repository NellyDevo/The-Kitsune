package kitsunemod.character;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import kitsunemod.KitsuneMod;
import kitsunemod.actions.ChangeShapeAction;
import kitsunemod.cards.basic.DancingLights;
import kitsunemod.cards.basic.Defend;
import kitsunemod.cards.basic.Strike;
import kitsunemod.cards.basic.Wink;
import kitsunemod.cards.basic.ChangeShape;
import kitsunemod.patches.AbstractCardEnum;
import kitsunemod.patches.KitsuneEnum;
import kitsunemod.powers.KitsuneShapePower;
import kitsunemod.relics.KitsuneRelic;
import kitsunemod.relics.WornPearl;
import kitsunemod.ui.EnergyOrbKitsune;

import java.util.ArrayList;
import java.util.HashMap;

public class KitsuneCharacter extends CustomPlayer {
    public static final int ENERGY_PER_TURN = 3;
    public static final String MY_CHARACTER_SHOULDER_2 = "kitsunemod/images/char/shoulder2.png";
    public static final String MY_CHARACTER_SHOULDER_1 = "kitsunemod/images/char/shoulder.png";
    public static final String MY_CHARACTER_CORPSE = "kitsunemod/images/char/corpse.png";
    public static final String SPINE_SKELETON = "kitsunemod/images/char/animations/TheKitsuneAnimations.json";
    public static final String SPINE_ATLAS = "kitsunemod/images/char/animations/TheKitsuneAnimations.atlas";
    private static final String ID = KitsuneMod.makeID("KitsuneCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;
    private static final float DIALOG_X_ADJUSTMENT = 0.0F;
    private static final float DIALOG_Y_ADJUSTMENT = 220.0F;
    private AnimationState.TrackEntry animationTrackEntry;
    private HashMap<String, Slot> animationSlots = new HashMap<>();
    private HashMap<String, Attachment> storedAssets = new HashMap<>();

    public KitsuneCharacter(String name) {
        super(name, KitsuneEnum.KITSUNE_CLASS, new EnergyOrbKitsune(), null, null);

        dialogX = drawX + DIALOG_X_ADJUSTMENT * Settings.scale;
        dialogY = drawY + DIALOG_Y_ADJUSTMENT * Settings.scale;

        loadAnimation(SPINE_ATLAS, SPINE_SKELETON, 1.0F);
        animationTrackEntry = state.setAnimation(0, "Idle", true);
        animationTrackEntry.setTime(animationTrackEntry.getEndTime() * MathUtils.random());
        animationTrackEntry.setTimeScale(1.0F);

        initializeClass(null,
                MY_CHARACTER_SHOULDER_2,
                MY_CHARACTER_SHOULDER_1,
                MY_CHARACTER_CORPSE,
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));
    }

    //I'm a Shapeshifter
    public void transformToFox() {
        animationSlots.get("Human Right Arm").setAttachment(null);
        animationSlots.get("Human Left Arm").setAttachment(null);
        animationSlots.get("Human Left Wrist").setAttachment(null);
        animationSlots.get("Human Body").setAttachment(null);
        animationSlots.get("Sword").setAttachment(null);

        animationSlots.get("Kitsune Hair").setAttachment(null);
        animationSlots.get("Kitsune Tail").setAttachment(null);
        animationSlots.get("Kitsune Left Ear").setAttachment(null);
        animationSlots.get("Kitsune Right Ear").setAttachment(null);

        animationSlots.get("Fox Pendant").setAttachment(storedAssets.get("Fox Pendant"));
        animationSlots.get("Fox Tail").setAttachment(storedAssets.get("Fox Tail"));
        animationSlots.get("Fox Body").setAttachment(storedAssets.get("Fox Body"));

        animationSlots.get("Ninetails Tail One").setAttachment(null);
        animationSlots.get("Ninetails Tail Two").setAttachment(null);
        animationSlots.get("Ninetails Tail Three").setAttachment(null);
        animationSlots.get("Ninetails Tail Four").setAttachment(null);
        animationSlots.get("Ninetails Tail Five").setAttachment(null);
        animationSlots.get("Ninetails Tail Six").setAttachment(null);
        animationSlots.get("Ninetails Tail Seven").setAttachment(null);
        animationSlots.get("Ninetails Tail Eight").setAttachment(null);
        animationSlots.get("Ninetails Tail Nine").setAttachment(null);
        animationSlots.get("Ninetails Hair").setAttachment(null);
    }

    //Chained down to my core
    public void transformToKitsune() {
        animationSlots.get("Human Right Arm").setAttachment(storedAssets.get("Human Right Arm"));
        animationSlots.get("Human Left Arm").setAttachment(storedAssets.get("Human Left Arm"));
        animationSlots.get("Human Left Wrist").setAttachment(storedAssets.get("Human Left Wrist"));
        animationSlots.get("Human Body").setAttachment(storedAssets.get("Human Body"));
        animationSlots.get("Sword").setAttachment(storedAssets.get("Sword"));

        animationSlots.get("Kitsune Hair").setAttachment(storedAssets.get("Kitsune Hair"));
        animationSlots.get("Kitsune Tail").setAttachment(storedAssets.get("Kitsune Tail"));
        animationSlots.get("Kitsune Left Ear").setAttachment(storedAssets.get("Kitsune Left Ear"));
        animationSlots.get("Kitsune Right Ear").setAttachment(storedAssets.get("Kitsune Right Ear"));

        animationSlots.get("Fox Pendant").setAttachment(null);
        animationSlots.get("Fox Tail").setAttachment(null);
        animationSlots.get("Fox Body").setAttachment(null);

        animationSlots.get("Ninetails Tail One").setAttachment(null);
        animationSlots.get("Ninetails Tail Two").setAttachment(null);
        animationSlots.get("Ninetails Tail Three").setAttachment(null);
        animationSlots.get("Ninetails Tail Four").setAttachment(null);
        animationSlots.get("Ninetails Tail Five").setAttachment(null);
        animationSlots.get("Ninetails Tail Six").setAttachment(null);
        animationSlots.get("Ninetails Tail Seven").setAttachment(null);
        animationSlots.get("Ninetails Tail Eight").setAttachment(null);
        animationSlots.get("Ninetails Tail Nine").setAttachment(null);
        animationSlots.get("Ninetails Hair").setAttachment(null);
    }

    //Please don't take off my mask
    public void transformToHuman() {
        animationSlots.get("Human Right Arm").setAttachment(storedAssets.get("Human Right Arm"));
        animationSlots.get("Human Left Arm").setAttachment(storedAssets.get("Human Left Arm"));
        animationSlots.get("Human Left Wrist").setAttachment(storedAssets.get("Human Left Wrist"));
        animationSlots.get("Human Body").setAttachment(storedAssets.get("Human Body"));
        animationSlots.get("Sword").setAttachment(storedAssets.get("Sword"));

        animationSlots.get("Kitsune Hair").setAttachment(null);
        animationSlots.get("Kitsune Tail").setAttachment(null);
        animationSlots.get("Kitsune Left Ear").setAttachment(null);
        animationSlots.get("Kitsune Right Ear").setAttachment(null);

        animationSlots.get("Fox Pendant").setAttachment(null);
        animationSlots.get("Fox Tail").setAttachment(null);
        animationSlots.get("Fox Body").setAttachment(null);

        animationSlots.get("Ninetails Tail One").setAttachment(null);
        animationSlots.get("Ninetails Tail Two").setAttachment(null);
        animationSlots.get("Ninetails Tail Three").setAttachment(null);
        animationSlots.get("Ninetails Tail Four").setAttachment(null);
        animationSlots.get("Ninetails Tail Five").setAttachment(null);
        animationSlots.get("Ninetails Tail Six").setAttachment(null);
        animationSlots.get("Ninetails Tail Seven").setAttachment(null);
        animationSlots.get("Ninetails Tail Eight").setAttachment(null);
        animationSlots.get("Ninetails Tail Nine").setAttachment(null);
        animationSlots.get("Ninetails Hair").setAttachment(null);
    }

    //my place to hide
    public void transformToNinetailed() {
        animationSlots.get("Human Right Arm").setAttachment(storedAssets.get("Human Right Arm"));
        animationSlots.get("Human Left Arm").setAttachment(storedAssets.get("Human Left Arm"));
        animationSlots.get("Human Left Wrist").setAttachment(storedAssets.get("Human Left Wrist"));
        animationSlots.get("Human Body").setAttachment(storedAssets.get("Human Body"));
        animationSlots.get("Sword").setAttachment(storedAssets.get("Sword"));

        animationSlots.get("Kitsune Hair").setAttachment(null);
        animationSlots.get("Kitsune Tail").setAttachment(null);
        animationSlots.get("Kitsune Left Ear").setAttachment(storedAssets.get("Kitsune Left Ear"));
        animationSlots.get("Kitsune Right Ear").setAttachment(storedAssets.get("Kitsune Right Ear"));

        animationSlots.get("Fox Pendant").setAttachment(null);
        animationSlots.get("Fox Tail").setAttachment(null);
        animationSlots.get("Fox Body").setAttachment(null);

        animationSlots.get("Ninetails Tail One").setAttachment(storedAssets.get("Ninetails Tail One"));
        animationSlots.get("Ninetails Tail Two").setAttachment(storedAssets.get("Ninetails Tail Two"));
        animationSlots.get("Ninetails Tail Three").setAttachment(storedAssets.get("Ninetails Tail Three"));
        animationSlots.get("Ninetails Tail Four").setAttachment(storedAssets.get("Ninetails Tail Four"));
        animationSlots.get("Ninetails Tail Five").setAttachment(storedAssets.get("Ninetails Tail Five"));
        animationSlots.get("Ninetails Tail Six").setAttachment(storedAssets.get("Ninetails Tail Six"));
        animationSlots.get("Ninetails Tail Seven").setAttachment(storedAssets.get("Ninetails Tail Seven"));
        animationSlots.get("Ninetails Tail Eight").setAttachment(storedAssets.get("Ninetails Tail Eight"));
        animationSlots.get("Ninetails Tail Nine").setAttachment(storedAssets.get("Ninetails Tail Nine"));
        animationSlots.get("Ninetails Hair").setAttachment(storedAssets.get("Ninetails Hair"));
    }

    public void onShapeChange(KitsuneMod.KitsuneShapes shape) {
        switch(shape) {
            case FOX:
                transformToFox();
                break;
            case KITSUNE:
                transformToKitsune();
                break;
            case HUMAN:
                transformToHuman();
                break;
            case NINETAILED:
                transformToNinetailed();
                break;
        }
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1]; //UPDATE BODY TEXT :(
    }

    @Override
    public Color getSlashAttackColor() {
        return KitsuneMod.kitsuneColor;
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[0];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.KITSUNE_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return KitsuneMod.kitsuneColor;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() { //TODO
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
                };
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        //.allMatch is short-circuiting, so technically the first relic that says no prevents further ones from triggering.
        //This is, at this time, intended - if that needs to change, change it here
        //The only relics slated to alter default shape are the 2 commons that alter starting shape and Luminous Pearl, and you can't have the last with the other two
        boolean useDefaultShape = relics.stream()
                .filter(relic -> relic instanceof KitsuneRelic)
                .allMatch(relic -> ((KitsuneRelic) relic).shouldAutoChangeShape());
        if (useDefaultShape) {
            AbstractDungeon.actionManager.addToBottom(new ChangeShapeAction(this, this, new KitsuneShapePower(this, this)));
        }
    }

    @Override
    public Color getCardTrailColor() {
        return KitsuneMod.kitsuneColor;
    }

    @Override
    public int getAscensionMaxHPLoss() { //TODO
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() { //TODO
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.sound.playA("ATTACK_FAST", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() { //TODO
        return "ATTACK_FIRE";
    }

    @Override
    public Texture getCustomModeCharacterButtonImage() {
        return ImageMaster.loadImage("kitsunemod/images/charSelect/customMode_kitsune.png");
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new KitsuneCharacter(name);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Wink.ID);
        retVal.add(DancingLights.ID);
        retVal.add(ChangeShape.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(WornPearl.ID);
        UnlockTracker.markRelicAsSeen(WornPearl.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                77, 77, 0, 99, 5, //starting hp, max hp, max wisps, starting gold, starting hand size
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        super.loadAnimation(atlasUrl, skeletonUrl, scale);

        //Initialize Human Form
        Slot rightArmSlot = skeleton.findSlot("Right_Arm"); //human, kitsune, ninetailed
        animationSlots.put("Human Right Arm", rightArmSlot);
        storedAssets.put("Human Right Arm", rightArmSlot.getAttachment());
        Slot leftArmSlot = skeleton.findSlot("Sword_Arm"); //human, kitsune, ninetailed
        animationSlots.put("Human Left Arm", leftArmSlot);
        storedAssets.put("Human Left Arm", leftArmSlot.getAttachment());
        Slot leftWristSlot = skeleton.findSlot("Wrist"); //human, kitsune, ninetailed
        animationSlots.put("Human Left Wrist", leftWristSlot);
        storedAssets.put("Human Left Wrist", leftWristSlot.getAttachment());
        Slot swordSlot = skeleton.findSlot("Sword"); //human, kitsune, ninetailed
        animationSlots.put("Sword", swordSlot);
        storedAssets.put("Sword", swordSlot.getAttachment());
        Slot bodySlot = skeleton.findSlot("Body"); //human, kitsune, ninetailed
        animationSlots.put("Human Body", bodySlot);
        storedAssets.put("Human Body", bodySlot.getAttachment());

        //Initialize Kitsune Form
        Slot hairSlot = skeleton.findSlot("Hair"); //kitsune
        animationSlots.put("Kitsune Hair", hairSlot);
        storedAssets.put("Kitsune Hair", hairSlot.getAttachment());
        Slot tailSlot = skeleton.findSlot("Tail"); //kitsune
        animationSlots.put("Kitsune Tail", tailSlot);
        storedAssets.put("Kitsune Tail", tailSlot.getAttachment());
        Slot leftEarSlot = skeleton.findSlot("Left_Ear"); //kitsune, ninetails
        animationSlots.put("Kitsune Left Ear", leftEarSlot);
        storedAssets.put("Kitsune Left Ear", leftEarSlot.getAttachment());
        Slot rightEarSlot = skeleton.findSlot("Right_Ear"); //kitsune, ninetails
        animationSlots.put("Kitsune Right Ear", rightEarSlot);
        storedAssets.put("Kitsune Right Ear", rightEarSlot.getAttachment());

        //Initialize Fox Form
        Slot foxPendantSlot = skeleton.findSlot("Fox_Pendant"); //fox
        animationSlots.put("Fox Pendant", foxPendantSlot);
        storedAssets.put("Fox Pendant", foxPendantSlot.getAttachment());
        Slot foxTailSlot = skeleton.findSlot("Fox_Tail"); //fox
        animationSlots.put("Fox Tail", foxTailSlot);
        storedAssets.put("Fox Tail", foxTailSlot.getAttachment());
        Slot foxBodySlot = skeleton.findSlot("Fox_Body"); //fox
        animationSlots.put("Fox Body", foxBodySlot);
        storedAssets.put("Fox Body", foxBodySlot.getAttachment());

        //Initialize Ninetailed Form
        Slot ninetailsTailOneSlot = skeleton.findSlot("Nine_Tail_1"); //ninetails
        animationSlots.put("Ninetails Tail One", ninetailsTailOneSlot);
        storedAssets.put("Ninetails Tail One", ninetailsTailOneSlot.getAttachment());
        Slot ninetailsTailTwoSlot = skeleton.findSlot("Nine_Tail_2"); //ninetails
        animationSlots.put("Ninetails Tail Two", ninetailsTailTwoSlot);
        storedAssets.put("Ninetails Tail Two", ninetailsTailTwoSlot.getAttachment());
        Slot ninetailsTailThreeSlot = skeleton.findSlot("Nine_Tail_3"); //ninetails
        animationSlots.put("Ninetails Tail Three", ninetailsTailThreeSlot);
        storedAssets.put("Ninetails Tail Three", ninetailsTailThreeSlot.getAttachment());
        Slot ninetailsTailFourSlot = skeleton.findSlot("Nine_Tail_4"); //ninetails
        animationSlots.put("Ninetails Tail Four", ninetailsTailFourSlot);
        storedAssets.put("Ninetails Tail Four", ninetailsTailFourSlot.getAttachment());
        Slot ninetailsTailFiveSlot = skeleton.findSlot("Nine_Tail_5"); //ninetails
        animationSlots.put("Ninetails Tail Five", ninetailsTailFiveSlot);
        storedAssets.put("Ninetails Tail Five", ninetailsTailFiveSlot.getAttachment());
        Slot ninetailsTailSixSlot = skeleton.findSlot("Nine_Tail_6"); //ninetails
        animationSlots.put("Ninetails Tail Six", ninetailsTailSixSlot);
        storedAssets.put("Ninetails Tail Six", ninetailsTailSixSlot.getAttachment());
        Slot ninetailsTailSevenSlot = skeleton.findSlot("Nine_Tail_7"); //ninetails
        animationSlots.put("Ninetails Tail Seven", ninetailsTailSevenSlot);
        storedAssets.put("Ninetails Tail Seven", ninetailsTailSevenSlot.getAttachment());
        Slot ninetailsTailEightSlot = skeleton.findSlot("Nine_Tail_8"); //ninetails
        animationSlots.put("Ninetails Tail Eight", ninetailsTailEightSlot);
        storedAssets.put("Ninetails Tail Eight", ninetailsTailEightSlot.getAttachment());
        Slot ninetailsTailNineSlot = skeleton.findSlot("Nine_Tail_9"); //ninetails
        animationSlots.put("Ninetails Tail Nine", ninetailsTailNineSlot);
        storedAssets.put("Ninetails Tail Nine", ninetailsTailNineSlot.getAttachment());
        Slot ninetailsHairSlot = skeleton.findSlot("Orange_Hair"); //ninetails
        animationSlots.put("Ninetails Hair", ninetailsHairSlot);
        storedAssets.put("Ninetails Hair", ninetailsHairSlot.getAttachment());

        transformToKitsune();
    }
}
