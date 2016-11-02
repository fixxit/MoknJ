/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.info;

import java.util.List;
import nl.fixx.asset.data.domain.AssetLink;

/**
 *
 * @author adriaan
 */
public class LinkResponse extends Response {

    private AssetLink link;

    private List<AssetLink> links;

    /**
     * @return the link
     */
    public AssetLink getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(AssetLink link) {
        this.link = link;
    }

    /**
     * @return the links
     */
    public List<AssetLink> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(List<AssetLink> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "LinkResponse{" + "link=" + getLink() + ", links=" + getLinks() + '}';
    }

}
