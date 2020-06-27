package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

public class NeverEndingSparklerRngPatches {

    public static Random rng;

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateSeeds"
    )
    public static class GenerateSeeds {
        public static void Postfix() {
            rng = getRandomDependingOnActNumber();
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "loadSeeds"
    )
    public static class LoadSeeds {
        public static void Postfix(SaveFile file) {
            rng = getRandomDependingOnActNumber();
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "loadSave",
            paramtypez = {SaveFile.class}
    )
    public static class LoadSave {
        public static void Postfix(AbstractDungeon abstractDungeon, SaveFile saveFile) {
            rng = getRandomDependingOnActNumber();
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "dungeonTransitionSetup"
    )
    public static class DungeonTransitionSetup {
        public static void Postfix() {
            rng = getRandomDependingOnActNumber();
        }
    }


    private static Random getRandomDependingOnActNumber() {
        return new Random(Settings.seed, 100 * AbstractDungeon.actNum);
    }

}
