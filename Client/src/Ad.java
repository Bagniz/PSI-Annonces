import java.io.Serializable;
import java.sql.Date;

public class Ad implements Serializable {
    private int id;
    private String title;
    private String description;
    private float price;
    private Category category;
    private Client postedBy;
    private Date postingDate;
    private boolean isReserved;

    public Ad() {
    }

    public Ad(int id, String title, String description, float price, Category category, Client postedBy, Date postingDate, boolean isReserved) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.postedBy = postedBy;
        this.postingDate = postingDate;
        this.isReserved = isReserved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Client getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Client postedBy) {
        this.postedBy = postedBy;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
}
