package appsshoppy.com.whosnext.model;

/**
 * Created by akshayacharya on 17/09/16.
 */
public class ServiceCategory {
    private String category;
    private boolean isSelected;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
