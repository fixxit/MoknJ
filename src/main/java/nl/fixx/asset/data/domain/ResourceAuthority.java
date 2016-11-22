package nl.fixx.asset.data.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adriaan
 */
public enum ResourceAuthority {
    ALL_ACCESS("Administrator rights"),
    ASSET("Access to assets"),
    TYPE("Access to templates"),
    RESOURCE("Access to employees"),
    LINK("Access to Audit trails");

    final String displayName;

    private ResourceAuthority(String value) {
        this.displayName = value;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    public static String[] authorities() {
        List<String> array = new ArrayList<>();
        for (ResourceAuthority auth : ResourceAuthority.values()) {
            if (!auth.equals(ResourceAuthority.ALL_ACCESS)) {
                array.add(auth.name());
            }
        }
        return array.toArray(new String[array.size()]);
    }

    public static ResourceAuthority authority(String value) {
        for (ResourceAuthority auth : ResourceAuthority.values()) {
            if (auth.displayName.equals(value)) {
                return auth;
            }
        }
        return null;
    }

}
