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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdItem adItem = (AdItem) o;

        if (adType != adItem.adType) return false;
        return adAttributesMap.equals(adItem.adAttributesMap);

    }

    @Override
    public int hashCode() {
        int result = adType.hashCode();
        result = 31 * result + adAttributesMap.hashCode();
        return result;
    }
}
