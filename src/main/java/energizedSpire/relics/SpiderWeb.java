package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.EntanglePower;
import energizedSpire.EnergizedSpireMod;

public class SpiderWeb extends CustomRelic {

    public static final String ID = "energizedSpire:SpiderWeb";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int ENTANGLED_TO_GAIN = 1;
    public static final int TURNS = 6;

    private static final PowerStrings ENTANGLED_POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(EntanglePower.POWER_ID);

    public SpiderWeb() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        refreshTips();
    }

    public void atTurnStart() {
        if (this.counter == -1) {
            this.counter += 2;
        } else {
            this.counter++;
        }
        if (this.counter == TURNS) {
            this.counter = 0;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, null, new EntanglePower(AbstractDungeon.player), 1));
            addToBot(new MakeTempCardInHandAction(AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.POWER).makeCopy(), false));
        }
    }

    @Override
    public void onEquip() {
        this.counter = 0;
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURNS + DESCRIPTIONS[1] + ENTANGLED_TO_GAIN + DESCRIPTIONS[2];
    }

    public void refreshTips() {
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        this.tips.add(1, new PowerTip(ENTANGLED_POWER_STRINGS.NAME, ENTANGLED_POWER_STRINGS.DESCRIPTIONS[0]));
    }

}
