package energizedSpire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.ArrayList;


public class RemoveOrbAction extends AbstractGameAction {
    private AbstractOrb orb;

    public RemoveOrbAction(AbstractOrb orb) {
        this.orb = orb;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {

            ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;

            orbs.removeIf(o -> o == orb);
            orbs.add(new EmptyOrbSlot());

            for (int i = 0; i < orbs.size(); ++i) {
                orbs.get(i).setSlot(i, AbstractDungeon.player.maxOrbs);
            }

            if (Settings.FAST_MODE) {
                this.isDone = true;
                return;
            }
        }

        this.tickDuration();
    }
}
