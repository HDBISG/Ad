package org.soundofhope.ad;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ok on 10/9/17.
 */

public class AdItem implements Serializable {

    // 是否显示过
    public boolean isRenderd = false;

    public AdType adType = null;

    // ad attributes;
    Map<String, Object> adAttributesMap = new HashMap<>();

}
