package co.edu.uniandes.csw.mpusedvehicle.persistence;

import co.edu.uniandes.csw.mp.ann.MPLoCAnn;
import co.edu.uniandes.csw.mpusedvehicle.entities.ProductEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @generated
 */
@Stateless
public class ProductPersistence extends CrudPersistence<ProductEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPersistence.class);

    /**
     * @generated
     */
    public ProductPersistence() {
        this.entityClass = ProductEntity.class;
    }

    public List<ProductEntity> getByVehicleName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "%" + name.toUpperCase() + "%");
        return executeListNamedQuery("Product.getByVehicleName", params);
    }

    public ProductEntity getCheaperProductByProvider(String nameProvider) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nameProvider", "%" + nameProvider.toUpperCase() + "%");
            List<ProductEntity> list = new ArrayList<ProductEntity>();
            list = executeListNamedQuery("Product.getCheaperProductByProvider", params);
            return list.get(0);
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
    
    public List<ProductEntity> getProductByProvider(String nameProvider) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nameProvider", "%" + nameProvider.toUpperCase() + "%");
            List<ProductEntity> list = new ArrayList<ProductEntity>();
            list = executeListNamedQuery("Product.getProductByProvider", params);
            return list;
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public ProductEntity getCheaperProductByVehicle(String nameVehicle) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nameVehicle", "%" + nameVehicle.toUpperCase() + "%");
            List<ProductEntity> list = new ArrayList<ProductEntity>();
            list = executeListNamedQuery("Product.getCheaperProductByVehicle", params);
            return list.get(0);
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public List<String> getVehiclesName() {
        List<String> list = new ArrayList<String>();
        try {
            list = executeListNamedQuery("Product.getVehiclesName");
            return list;
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return list;
        }
    }

    public List<String> getVehiclesBrand() {
        List<String> list = new ArrayList<String>();
        try {
            list = executeListNamedQuery("Product.getVehiclesBrand");
            return list;
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return list;
        }
    }

    public List<String> getVehiclesCapacity() {
        List<String> list = new ArrayList<String>();
        try {
            List<Integer> listInteger = new ArrayList<Integer>();
            listInteger = executeListNamedQuery("Product.getVehiclesCapacity");
            for (Integer temp : listInteger) {
                list.add(temp.toString());
            }
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;
    }

    public List<String> getVehiclesColor() {
        List<String> list = new ArrayList<String>();
        try {
            list = executeListNamedQuery("Product.getVehiclesColor");
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;
    }

    public List<String> getVehiclesModel() {
        List<String> list = new ArrayList<String>();
        try {
            list = executeListNamedQuery("Product.getVehiclesModel");
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;
    }

    public List<String> getVehiclesPlate() {
            List<String> list = new ArrayList<String>();
            list.add("Even");
            list.add("Odd");
            return list;
    }

    public List<String> getVehiclesLocation() {
         List<String> list = new ArrayList<String>();
        try {
            list = executeListNamedQuery("Product.getVehiclesLocation");
            return list;
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return list;
        }
    }
    
    @MPLoCAnn(tier="Backend", reqId="R15")
    public List<ProductEntity> getProductsByAdvancedSearch(String brand, String model, Integer capacity, Integer price, String color, String plate, String location) {

        try {

            int startPrice = 0;
            int endPrice = 9999999;
            // Constructing the sql query
            String sql = " SELECT p "
                    + "FROM ProductEntity p "
                    + "WHERE    1 = 1 ";
            if (brand != null && !"".equalsIgnoreCase(brand)) {
                sql += "            AND UPPER(p.vehicle.brand) like UPPER(:brand) ";
            }
            if (model != null && !"".equalsIgnoreCase(model)) {
                sql += "            AND p.vehicle.model = :model ";
            }
            if (color != null && !"".equalsIgnoreCase(color)) {
                sql += "            AND p.vehicle.color = :color ";
            }
            if (plate != null && !"".equalsIgnoreCase(plate)) {
                if (plate.equals("Even")) {
                    sql += "            AND p.vehicle.plate = TRUE ";
                } else {
                    sql += "            AND p.vehicle.plate = FALSE ";
                }
            }
            if (capacity != null && capacity > 0) {
                sql += "            AND p.vehicle.capacity = :capacity ";
            }
            if (location != null && !"".equalsIgnoreCase(location)) {
                sql += "            AND p.vehicle.location = :location ";
            }
            if (price != null && price > 0) {
                sql += "            AND p.price between :startPrice AND :endPrice ";
                switch (price) {
                    case 2:
                        startPrice = 10000000;
                        endPrice = 99000000;
                        break;
                    case 3:
                        startPrice = 99000000;
                        endPrice = 999999999;
                        break;
                    default:
                        break;
                }
            }
            sql += "ORDER BY p.price";

            // Loading the sql query and setting parameters
            Query query = em.createQuery(sql);
            if (brand != null && !"".equalsIgnoreCase(brand)) {
                query.setParameter("brand", "%" + brand + "%");
            }
            if (model != null && !"".equalsIgnoreCase(model)) {
                query.setParameter("model", model);
            }
            if (color != null && !"".equalsIgnoreCase(color)) {
                query.setParameter("color", color);
            }
            if (location != null && !"".equalsIgnoreCase(location)) {
                query.setParameter("location", location);
            }
            if (capacity != null && capacity > 0) {
                query.setParameter("capacity", capacity);
            }
            if (price != null && price > 0) {
                query.setParameter("startPrice", startPrice);
                query.setParameter("endPrice", endPrice);
            }

            // Executing the query to get products
            List<ProductEntity> products = new ArrayList<ProductEntity>();
            products = query.getResultList();

            return products;

        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<ProductEntity>();
        }
    }
    
    public Boolean findProductPrurchasedByClient(Long idProduct, Long idClient){
       try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("client_id", idClient);
            params.put("product_id", idProduct);
            List<ProductEntity> list = executeListNamedQuery("Product.findProductPrurchasedByClient", params);
            return !list.isEmpty();
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }
    
    public int updatePurchasedByClient(Long idProduct, Long idClient) {
        Query q = em.createNativeQuery("INSERT INTO purchaseditems (productid, clientid) VALUES ("+idProduct+","+ idClient+")");        
        return q.executeUpdate();
    }
    public int deletePurchasedByClient(Long idProduct, Long idClient) {
        Query q = em.createNativeQuery("DELETE FROM purchaseditems WHERE productid ="+idProduct+" and clientid="+ idClient);        
        return q.executeUpdate();
    }
}
