package energizedSpire.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AbstractGameEffectDelayedByAnotherOneEffect extends AbstractGameEffect {

    private AbstractGameEffect effect;
    private AbstractGameEffect delayingEffect;

    public AbstractGameEffectDelayedByAnotherOneEffect(AbstractGameEffect effect, AbstractGameEffect delayingEffect) {
        this.effect = effect;
        this.delayingEffect = delayingEffect;

        AbstractDungeon.topLevelEffectsQueue.add(delayingEffect);
    }

    @Override
    public void update() {
        if (this.delayingEffect.isDone) {
            AbstractDungeon.topLevelEffectsQueue.add(effect);
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    @Override
    public void dispose() {
    }
}
