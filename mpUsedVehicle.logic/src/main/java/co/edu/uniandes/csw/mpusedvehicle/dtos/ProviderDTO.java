package co.edu.uniandes.csw.mpusedvehicle.dtos;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * @generated
 */
@XmlRootElement 
public class ProviderDTO {

    private Long id;
    private String name;
    private String userId;
    @PodamExclude
    private List<ProductDTO> products;
    private String email;
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    /**
     * @generated
     */
    public Long getId() {
        return id;
    }

    /**
     * @generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * @generated
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @generated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @generated
     */
    public void setUserId(String userid) {
        this.userId = userid;
    }

    /**
     * @generated
     */
    public List<ProductDTO> getProducts() {
        return products;
    }

    /**
     * @generated
     */
    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
