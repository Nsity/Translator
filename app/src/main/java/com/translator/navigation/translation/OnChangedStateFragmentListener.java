package com.translator.navigation.translation;

import com.translator.navigation.Translation;

/**
 * Created by fedorova on 04.04.2017.
 */

public interface OnChangedStateFragmentListener {

    public void updateFragmentState(int action, Translation translation);

}
