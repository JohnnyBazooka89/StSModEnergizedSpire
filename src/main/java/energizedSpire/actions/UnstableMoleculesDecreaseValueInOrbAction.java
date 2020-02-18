package energizedSpire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import energizedSpire.patches.UnstableMoleculesPatches;

public class UnstableMoleculesDecreaseValueInOrbAction extends AbstractGameAction {
    private AbstractOrb orb;

    public UnstableMoleculesDecreaseValueInOrbAction(AbstractOrb orb) {
        this.orb = orb;
    }

    public void update() {
        Integer value = UnstableMoleculesPatches.UnstableMoleculesFieldPatch.value.get(orb);
        if (value != null && value != 0) {
            int newValue = value - 1;
            UnstableMoleculesPatches.UnstableMoleculesFieldPatch.value.set(orb, newValue);
            if (newValue == 0) {
                AbstractDungeon.actionManager.addToBottom(new UnstableMoleculesRemoveOrbAction(orb));
            }
        }
        this.isDone = true;
    }
}
