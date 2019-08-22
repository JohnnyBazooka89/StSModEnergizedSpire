package energizedSpire.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.BaseModCardTags;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import energizedSpire.EnergizedSpireMod;

public class HugeHouse extends CustomRelic {

    public static final String ID = "energizedSpire:HugeHouse";
    public static final Texture IMG = ImageMaster.loadImage(EnergizedSpireMod.getRelicImagePath(ID));
    public static final Texture OUTLINE = ImageMaster.loadImage(EnergizedSpireMod.getRelicOutlineImagePath(ID));

    public static final int POTIONS_TO_LOSE = 1;
    public static final int GOLD_TO_LOSE = 50;
    public static final int MAX_HP_TO_LOSE = 5;
    public static final int CURSES_TO_OBTAIN = 1;
    public static final int STRIKES_TO_OBTAIN = 1;

    public HugeHouse() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;

        loseARandomPotion();
        loseGold();
        lowerMaxHP();
        obtainAStrike();
        obtainACurse();
    }

    private void loseARandomPotion() {
        AbstractPotion randomPotion = AbstractDungeon.player.getRandomPotion();
        if (randomPotion != null) {
            AbstractDungeon.player.removePotion(randomPotion);
        }
    }

    private void loseGold() {
        AbstractDungeon.player.loseGold(GOLD_TO_LOSE);
    }

    private void lowerMaxHP() {
        AbstractDungeon.player.decreaseMaxHealth(MAX_HP_TO_LOSE);
    }

    private void obtainAStrike() {
        AbstractCard strike = getClassSpecificStrike();
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(strike, Settings.WIDTH * 0.4F, Settings.HEIGHT / 2.0F));
    }

    private void obtainACurse() {
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), Settings.WIDTH * 0.6F, Settings.HEIGHT / 2.0F));
    }

    private AbstractCard getClassSpecificStrike() {
        for (AbstractCard card : CardLibrary.getAllCards()) {
            if (card.color == AbstractDungeon.player.getCardColor() && card.hasTag(BaseModCardTags.BASIC_STRIKE)) {
                return card;
            }
        }
        return new Strike_Red();
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POTIONS_TO_LOSE + DESCRIPTIONS[1] + GOLD_TO_LOSE + DESCRIPTIONS[2] + MAX_HP_TO_LOSE + DESCRIPTIONS[3] + CURSES_TO_OBTAIN + DESCRIPTIONS[4] + STRIKES_TO_OBTAIN + DESCRIPTIONS[5];
    }

    @Override
    public void initializeTips() {
        super.initializeTips();
        this.tips.removeIf(tip -> tip.header.equalsIgnoreCase("strike"));
    }
}