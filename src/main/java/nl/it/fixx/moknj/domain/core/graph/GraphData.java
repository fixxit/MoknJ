package nl.it.fixx.moknj.domain.core.graph;

/**
 *
 * @author adriaan
 */
public class GraphData {

    private String[] xlabels; // x axis
    private String[] ylabels; // y axis
    private int[][] graphData;
    private String graphTitle;
    private String graphStyle;

    /**
     * @return the graphTitle
     */
    public String getGraphTitle() {
        return graphTitle;
    }

    /**
     * @param graphTitle the graphTitle to set
     */
    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }



    /**
     * @return the graphData
     */
    public int[][] getGraphData() {
        return graphData;
    }

    /**
     * @param graphData the graphData to set
     */
    public void setGraphData(int[][] graphData) {
        this.graphData = graphData;
    }

    /**
     * @return the xlabels
     */
    public String[] getXlabels() {
        return xlabels;
    }

    /**
     * @param xlabels the xlabels to set
     */
    public void setXlabels(String[] xlabels) {
        this.xlabels = xlabels;
    }

    /**
     * @return the ylabels
     */
    public String[] getYlabels() {
        return ylabels;
    }

    /**
     * @param ylabels the ylabels to set
     */
    public void setYlabels(String[] ylabels) {
        this.ylabels = ylabels;
    }

    /**
     * @return the graphStyle
     */
    public String getGraphStyle() {
        return graphStyle;
    }

    /**
     * @param graphStyle the graphStyle to set
     */
    public void setGraphStyle(String graphStyle) {
        this.graphStyle = graphStyle;
    }

}
