package app.discountcheo.com.discountcheo;

/**
 * Created by Faisal on 7/12/2015.
 */
public class JsonData {
    private String client_system_number,discount_amount, og_image,facebook_page_url;
    private String userId;
    private int facebook_id;

    public JsonData(){

    }

    public JsonData(int facebook_id, String userId, String facebook_page_url, String og_image, String discount_amount, String client_system_number) {
        this.facebook_id = facebook_id;
        this.userId = userId;
        this.facebook_page_url = facebook_page_url;
        this.og_image = og_image;
        this.discount_amount = discount_amount;
        this.client_system_number = client_system_number;
    }

    public String getClient_system_number() {
        return client_system_number;
    }

    public void setClient_system_number(String client_system_number) {
        this.client_system_number = client_system_number;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getOg_image() {
        return og_image;
    }

    public void setOg_image(String og_image) {
        this.og_image = og_image;
    }

    public String getFacebook_page_url() {
        return facebook_page_url;
    }

    public void setFacebook_page_url(String facebook_page_url) {
        this.facebook_page_url = facebook_page_url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(int facebook_id) {
        this.facebook_id = facebook_id;
    }
}
