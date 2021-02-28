package energizedSpire.patches;

import com.blanktheevil.blockreminder.BlockPreview;

public class UnstableMoleculesBlockReminderInteraction {

    public final static String BLOCK_REMINDER_MOD_ID = "block-reminder";

    public static boolean isPreview() {
        return BlockPreview.isPreview;
    }

}