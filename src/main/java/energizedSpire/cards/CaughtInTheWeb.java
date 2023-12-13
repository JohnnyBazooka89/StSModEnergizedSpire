package energizedSpire.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import energizedSpire.EnergizedSpireMod;

public class CaughtInTheWeb extends CustomCard {
    public static final String ID = "energizedSpire:CaughtInTheWeb";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;

    public CaughtInTheWeb() {
        super(ID, NAME, EnergizedSpireMod.getCardImagePath(ID), COST, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.exhaust = true;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public boolean canPlay(AbstractCard card) {
        return (card.type != AbstractCard.CardType.ATTACK);
    }

    public void upgrade() {
    }
}
