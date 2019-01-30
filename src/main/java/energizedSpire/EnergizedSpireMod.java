package energizedSpire;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import energizedSpire.relics.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class EnergizedSpireMod implements PostInitializeSubscriber, EditRelicsSubscriber, EditStringsSubscriber {

    public static final Logger logger = LogManager.getLogger(EnergizedSpireMod.class.getName());

    //Mod metadata
    private static final String MOD_NAME = "Energized Spire";
    private static final String AUTHOR = "JohnnyBazooka89";
    private static final String DESCRIPTION = "";

    //Badge
    private static final String BADGE_IMG = "energizedSpire/img/ModBadge.png";

    //Localization strings
    private static final String RELIC_STRINGS_PATH = "energizedSpire/localization/RelicStrings.json";

    public EnergizedSpireMod() {
        BaseMod.subscribe(this);
    }

    public static String getRelicImagePath(String cardID) {
        return "energizedSpire/img/relics/" + cardID.replaceFirst("energizedSpire:", "") + ".png";
    }

    public static String getRelicOutlineImagePath(String cardID) {
        return "energizedSpire/img/relics/outlines/" + cardID.replaceFirst("energizedSpire:", "") + ".png";
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
        BaseMod.addRelic(new DeflatedTyre(), RelicType.RED);
        BaseMod.addRelic(new HighHeels(), RelicType.GREEN);
        BaseMod.addRelic(new OldTV(), RelicType.BLUE);
        BaseMod.addRelic(new CursedPentagram(), RelicType.SHARED);
        BaseMod.addRelic(new DeadBattery(), RelicType.SHARED);
        BaseMod.addRelic(new RedRose(), RelicType.SHARED);
        BaseMod.addRelic(new SpiderWeb(), RelicType.SHARED);
        BaseMod.addRelic(new HugeHouse(), RelicType.SHARED);

        logger.info("Done editing relics");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Begin editing strings");

        //Relic Strings
        BaseMod.loadCustomStringsFile(RelicStrings.class, RELIC_STRINGS_PATH);

        logger.info("Done editing strings");
    }

}
