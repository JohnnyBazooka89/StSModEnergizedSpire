package energized_spire.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import energized_spire.relics.OgreHead;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
public class OgreHeadPatch {

    public static void Prefix(AbstractPlayer player, AbstractCard c, @ByRef AbstractMonster[] monster, int energyOnUse) {
        AbstractRelic ogreHeadRelic = player.getRelic(OgreHead.ID);
        if (ogreHeadRelic != null && monster[0] != null) {
            int monstersAmount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
            List<AbstractMonster> otherMonstersToTarget = new ArrayList<>();
            for (int i = 0; i < monstersAmount; i++) {
                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (!m.isDeadOrEscaped() && m != monster[0]) {
                    otherMonstersToTarget.add(m);
                }
            }
            if (!otherMonstersToTarget.isEmpty() && AbstractDungeon.cardRandomRng.randomBoolean()) {
                ogreHeadRelic.flash();
                monster[0] = otherMonstersToTarget.get(AbstractDungeon.cardRandomRng.random(otherMonstersToTarget.size() - 1));
            }
        }
    }

}
