package co.edu.uniandes.csw.mpusedvehicle.dtos;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @generated
 */
@XmlRootElement 
public class VehicleDTO {

    private Long id;
    private String name;
    private String description;
    private String color;
    private String model;
    private Integer capacity;
    private String brand;
    private String image;
    private List<ReviewDTO> reviews;
    private Boolean plate;
    private String location; 
            
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
    public String getDescription() {
        return description;
    }

    /**
     * @generated
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @generated
     */
    public String getColor() {
        return color;
    }

    /**
     * @generated
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @generated
     */
    public String getModel() {
        return model;
    }

    /**
     * @generated
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @generated
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @generated
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @generated
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @generated
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @generated
     */
    public String getImage() {
        return image;
    }

    /**
     * @generated
     */
    public void setImage(String image) {
        this.image = image;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public Boolean getPlate() {
        return plate;
    }

    public void setPlate(Boolean plate) {
        this.plate = plate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    

}
