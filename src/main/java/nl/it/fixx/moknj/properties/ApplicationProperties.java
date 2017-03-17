package nl.it.fixx.moknj.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adriaan
 */
@Component
@ConfigurationProperties
public class ApplicationProperties {

    private String environment;
    private System system;
    private Admin admin;
    private Security security;

    /**
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * @param environment the environment to set
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public static class System {

        private String db;
        private String url;
        private String port;
        private String username;
        private String password;

        /**
         * @return the db
         */
        public String getDb() {
            return db;
        }

        /**
         * @param db the db to set
         */
        public void setDb(String db) {
            this.db = db;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return the port
         */
        public String getPort() {
            return port;
        }

        /**
         * @param port the port to set
         */
        public void setPort(String port) {
            this.port = port;
        }

        /**
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username the username to set
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * @param password the password to set
         */
        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "System{" + "db=" + db + ", url=" + url + ", port=" + port + ", username=" + username + ", password=" + password + '}';
        }
    }

    public static class Admin {

        private String user;
        private String pass;

        /**
         * @return the user
         */
        public String getUser() {
            return user;
        }

        /**
         * @param user the user to set
         */
        public void setUser(String user) {
            this.user = user;
        }

        /**
         * @return the pass
         */
        public String getPass() {
            return pass;
        }

        /**
         * @param pass the pass to set
         */
        public void setPass(String pass) {
            this.pass = pass;
        }

        @Override
        public String toString() {
            return "Admin{" + "user=" + user + ", pass=" + pass + '}';
        }
    }

    public static class Security {

        private String realm;
        private String client;
        private String secret;
        private String tokenValiditySeconds;
        private String refreshTokenValiditySeconds;
        private String resourceId;

        /**
         * @return the realm
         */
        public String getRealm() {
            return realm;
        }

        /**
         * @param realm the realm to set
         */
        public void setRealm(String realm) {
            this.realm = realm;
        }

        /**
         * @return the client
         */
        public String getClient() {
            return client;
        }

        /**
         * @param client the client to set
         */
        public void setClient(String client) {
            this.client = client;
        }

        /**
         * @return the secret
         */
        public String getSecret() {
            return secret;
        }

        /**
         * @param secret the secret to set
         */
        public void setSecret(String secret) {
            this.secret = secret;
        }

        /**
         * @return the tokenValiditySeconds
         */
        public String getTokenValiditySeconds() {
            return tokenValiditySeconds;
        }

        /**
         * @param tokenValiditySeconds the tokenValiditySeconds to set
         */
        public void setTokenValiditySeconds(String tokenValiditySeconds) {
            this.tokenValiditySeconds = tokenValiditySeconds;
        }

        /**
         * @return the refreshTokenValiditySeconds
         */
        public String getRefreshTokenValiditySeconds() {
            return refreshTokenValiditySeconds;
        }

        /**
         * @param refreshTokenValiditySeconds the refreshTokenValiditySeconds to
         * set
         */
        public void setRefreshTokenValiditySeconds(String refreshTokenValiditySeconds) {
            this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        }

        /**
         * @return the resourceId
         */
        public String getResourceId() {
            return resourceId;
        }

        /**
         * @param resourceId the resourceId to set
         */
        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        public String toString() {
            return "Security{" + "realm=" + realm + ", client=" + client + ", secret=" + secret + ", tokenValiditySeconds=" + tokenValiditySeconds + ", refreshTokenValiditySeconds=" + refreshTokenValiditySeconds + ", resourceId=" + resourceId + '}';
        }
    }

    /**
     * @return the system
     */
    public System getSystem() {
        return system;
    }

    /**
     * @param system the system to set
     */
    public void setSystem(System system) {
        this.system = system;
    }

    /**
     * @return the admin
     */
    public Admin getAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    /**
     * @return the security
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * @param security the security to set
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    @Override
    public String toString() {
        return "ApplicationProperties{" + "environment=" + environment + ", system=" + system + ", admin=" + admin + ", security=" + security + '}';
    }

}
