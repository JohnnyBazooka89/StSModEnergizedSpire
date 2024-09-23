package energizedSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.NeutralStance;
import energizedSpire.relics.BlackCloud;
import energizedSpire.stances.DepressionStance;
import javassist.CtBehavior;

@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
public class BlackCloudPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(GameActionManager gameActionManager) {
        if (AbstractDungeon.player.hasRelic(BlackCloud.ID)) {
            gameActionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    if(AbstractDungeon.player.stance.ID.equals(NeutralStance.STANCE_ID)){
                        AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new DepressionStance()));
                    }
                }
            });
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.NewExprMatcher(EnableEndTurnButtonAction.class);
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
