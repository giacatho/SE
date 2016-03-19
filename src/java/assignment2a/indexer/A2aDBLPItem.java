package assignment2a.indexer;

/**
 *
 * @author a
 */
public class A2aDBLPItem {
    private String title;
    private String pubyear;

    public A2aDBLPItem(String type) {
    }

    //setter and getter
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    //setter and getter
    public void setPubyear(String pubyear) {
        this.pubyear = pubyear;
    }

    public String getPubyear() {
        return pubyear;
    }

    @Override
    public String toString() {
        return String.format("title:%s\npubyear:%s\n\n", 
				this.title, this.pubyear);
    }
}
