package com.translator.system.database;

import android.content.Context;

import com.translator.navigation.Translation;

/**
 * Created by nsity on 21.03.17.
 */

public class HistoryDBInterface extends ADBWorker {



    public HistoryDBInterface(Context context) {
        super(context);
    }

    public int saveTranslation(Translation translation) {
        return 0;
    }
}
