package co.edu.uniandes.csw.mpusedvehicle.dtos;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author d.jimenez13
 */
@XmlRootElement 
public class AdminDTO {
    
    private Long id;
    private String name;
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
