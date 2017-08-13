package nl.it.fixx.moknj.properties.enums;

public class Database {

    public static final String ENVIRONMENT_OPENSHIFT = "openshift";

    public enum OpenShiftEnv {
        OPENSHIFT_MONGODB_DB_HOST,
        OPENSHIFT_MONGODB_DB_PORT,
        OPENSHIFT_MONGODB_DB_USERNAME,
        OPENSHIFT_MONGODB_DB_PASSWORD;

        @Override
        public String toString() {
            return System.getenv(this.name());
        }
    }
}
