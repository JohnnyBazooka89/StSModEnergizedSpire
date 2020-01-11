package energizedSpire;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import energizedSpire.cards.ReceptionProblems;
import energizedSpire.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpireInitializer
public class EnergizedSpireMod implements PostInitializeSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber {

    public static final Logger logger = LogManager.getLogger(EnergizedSpireMod.class.getName());

    //Mod metadata
    private static final String MOD_NAME = "Energized Spire";
    private static final String AUTHOR = "JohnnyBazooka89";
    private static final String DESCRIPTION = "";

    //Badge
    private static final String BADGE_IMG = "energizedSpire/img/ModBadge.png";

    //Localization strings
    private static final String RELIC_STRINGS_PATH = "energizedSpire/localization/RelicStrings.json";
    private static final String CARD_STRINGS_PATH = "energizedSpire/localization/CardStrings.json";
    private static final String KEYWORD_STRINGS_PATH = "energizedSpire/localization/KeywordStrings.json";
    private static final String POWER_STRINGS_PATH = "energizedSpire/localization/PowerStrings.json";
    private static final String STANCE_STRINGS_PATH = "energizedSpire/localization/StanceStrings.json";

    public EnergizedSpireMod() {
        BaseMod.subscribe(this);
    }

    public static String getRelicImagePath(String cardID) {
        return "energizedSpire/img/relics/" + cardID.replaceFirst("energizedSpire:", "") + ".png";
    }

    public static String getRelicOutlineImagePath(String cardID) {
        return "energizedSpire/img/relics/outlines/" + cardID.replaceFirst("energizedSpire:", "") + ".png";
    }

    public static String getCardImagePath(String cardId) {
        return "energizedSpire/img/cards/" + cardId.replaceFirst("energizedSpire:", "") + ".png";
    }

    public static String getPower128ImagePath(String powerId) {
        return "energizedSpire/img/powers/128/" + powerId.replaceFirst("energizedSpire:", "").replaceFirst("Power", "") + ".png";
    }

    public static String getPower48ImagePath(String powerId) {
        return "energizedSpire/img/powers/48/" + powerId.replaceFirst("energizedSpire:", "").replaceFirst("Power", "") + ".png";
    }

    public static void initialize() {
        logger.info("======================= ENERGIZED SPIRE INIT =======================");

        new EnergizedSpireMod();

        logger.info("====================================================================");
    }

    @Override
    public void receivePostInitialize() {
        // Mod badge
        Texture badgeTexture = ImageMaster.loadImage(BADGE_IMG);
        ModPanel settingsPanel = new ModPanel();
        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, settingsPanel);
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Begin editing relics");

        // Add relics
        BaseMod.addRelic(new DecayingSkull(), RelicType.RED);
        BaseMod.addRelic(new HighHeels(), RelicType.GREEN);
        BaseMod.addRelic(new OldTV(), RelicType.BLUE);
        BaseMod.addRelic(new BlackCloud(), RelicType.PURPLE);
        BaseMod.addRelic(new TabascoSauce(), RelicType.RED);
        BaseMod.addRelic(new PogoStick(), RelicType.GREEN);
        BaseMod.addRelic(new UnstableMolecules(), RelicType.BLUE);
        BaseMod.addRelic(new CrystalSphere(), RelicType.PURPLE);
        BaseMod.addRelic(new CursedPentagram(), RelicType.SHARED);
        BaseMod.addRelic(new DeadBattery(), RelicType.SHARED);
        BaseMod.addRelic(new RedRose(), RelicType.SHARED);
        BaseMod.addRelic(new SpiderWeb(), RelicType.SHARED);
        BaseMod.addRelic(new HugeHouse(), RelicType.SHARED);
        BaseMod.addRelic(new OgreHead(), RelicType.SHARED);
        BaseMod.addRelic(new EnergizedDie(), RelicType.SHARED);
        BaseMod.addRelic(new MagnifyingGlass(), RelicType.SHARED);
        BaseMod.addRelic(new StickyHand(), RelicType.SHARED);

        logger.info("Done editing relics");
    }


    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new ReceptionProblems());
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Begin editing strings");

        //Relic Strings
        BaseMod.loadCustomStringsFile(RelicStrings.class, RELIC_STRINGS_PATH);
        //Card Strings
        BaseMod.loadCustomStringsFile(CardStrings.class, CARD_STRINGS_PATH);
        //Keyword Strings
        BaseMod.loadCustomStringsFile(KeywordStrings.class, KEYWORD_STRINGS_PATH);
        //Power Strings
        BaseMod.loadCustomStringsFile(PowerStrings.class, POWER_STRINGS_PATH);
        //Stance Strings
        BaseMod.loadCustomStringsFile(StanceStrings.class, STANCE_STRINGS_PATH);

        logger.info("Done editing strings");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal(KEYWORD_STRINGS_PATH).readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {
        }.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k, v) -> BaseMod.addKeyword("energizedSpire:".toLowerCase(), v.PROPER_NAME, v.NAMES, v.DESCRIPTION));
    }

}
