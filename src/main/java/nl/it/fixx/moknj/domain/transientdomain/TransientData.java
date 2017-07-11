package nl.it.fixx.moknj.domain.transientdomain;

public class TransientData {

    private String freeDate;
    private String freeValue;
    private String menu;
    private String employee;
    private String menuId;
    private String token;
    private boolean cascade;

    /**
     * @return the freeDate
     */
    public String getFreeDate() {
        return freeDate;
    }

    /**
     * @param freeDate the freeDate to set
     */
    public void setFreeDate(String freeDate) {
        this.freeDate = freeDate;
    }

    /**
     * @return the freeValue
     */
    public String getFreeValue() {
        return freeValue;
    }

    /**
     * @param freeValue the freeValue to set
     */
    public void setFreeValue(String freeValue) {
        this.freeValue = freeValue;
    }

    /**
     * @return the menu
     */
    public String getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     * @return the employee
     */
    public String getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(String employee) {
        this.employee = employee;
    }

    /**
     * @return the menuId
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * @param menuId the menuId to set
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the cascade
     */
    public boolean isCascade() {
        return cascade;
    }

    /**
     * @param cascade the cascade to set
     */
    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }
}
