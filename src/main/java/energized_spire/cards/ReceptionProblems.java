package energized_spire.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import energized_spire.EnergizedSpireMod;
import energized_spire.powers.RegainFocusPower;

public class ReceptionProblems extends CustomCard {
    public static final String ID = "energizedSpire:ReceptionProblems";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = -2;
    private static final int FOCUS_TO_LOSE_AND_REGAIN = 2;

    public ReceptionProblems() {
        super(ID, NAME, EnergizedSpireMod.getCardImagePath(ID), COST, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, AbstractCard.CardTarget.NONE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, -FOCUS_TO_LOSE_AND_REGAIN), -FOCUS_TO_LOSE_AND_REGAIN));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegainFocusPower(AbstractDungeon.player, FOCUS_TO_LOSE_AND_REGAIN), FOCUS_TO_LOSE_AND_REGAIN));
    }

    public void upgrade() {
    }
}
