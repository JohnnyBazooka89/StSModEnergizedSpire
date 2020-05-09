package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import energizedSpire.EnergizedSpireMod;

public class MagnifyingGlass extends CustomRelic {

    public static final String ID = "energizedSpire:MagnifyingGlass";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int MORE_HP_PERCENT = 25;

    public MagnifyingGlass() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            m.increaseMaxHp(m.maxHealth * MORE_HP_PERCENT / 100, true);
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(m, this));
        }
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MORE_HP_PERCENT + DESCRIPTIONS[1];
    }
}
