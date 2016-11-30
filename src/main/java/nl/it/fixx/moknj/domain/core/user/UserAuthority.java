package nl.it.fixx.moknj.domain.core.user;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adriaan
 */
public enum UserAuthority {
    ALL_ACCESS("Administrator rights"),
    ASSET("Access to edit/create/delete of assets"),
    MENU("Access to edit/create/delete of Menu's"),
    TYPE("Access to edit/create/delete of templates"),
    RESOURCE("Access to edit/create/delete of system users"),
    LINK("Access to edit/create of Audit trails");

    final String displayName;

    private UserAuthority(String value) {
        this.displayName = value;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    public static String[] authorities() {
        List<String> array = new ArrayList<>();
        for (UserAuthority auth : UserAuthority.values()) {
            if (!auth.equals(UserAuthority.ALL_ACCESS)) {
                array.add(auth.name());
            }
        }
        return array.toArray(new String[array.size()]);
    }

    public static UserAuthority authority(String value) {
        for (UserAuthority auth : UserAuthority.values()) {
            if (auth.displayName.equals(value)) {
                return auth;
            }
        }
        return null;
    }

}
