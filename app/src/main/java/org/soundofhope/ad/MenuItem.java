package org.soundofhope.ad;

/**
 * The {@link MenuItem} class.
 * <p>Defines the attributes for a restaurant menu item.</p>
 */
class MenuItem {

    String seq;

    public MenuItem() {

    }

    public MenuItem( String seq ) {
        this.seq = seq;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
