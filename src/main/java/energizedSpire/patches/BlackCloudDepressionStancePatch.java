package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.stances.AbstractStance;
import energizedSpire.stances.DepressionStance;

@SpirePatch(clz = AbstractStance.class, method = "getStanceFromName")
public class BlackCloudDepressionStancePatch {

    public static SpireReturn<AbstractStance> Prefix(String name) {
        if (name.equals(DepressionStance.STANCE_ID)) {
            return SpireReturn.Return(new DepressionStance());
        }
        return SpireReturn.Continue();
    }

}