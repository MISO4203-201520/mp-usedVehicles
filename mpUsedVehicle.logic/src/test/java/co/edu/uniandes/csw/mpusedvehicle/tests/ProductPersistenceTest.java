package co.edu.uniandes.csw.mpusedvehicle.tests;

import co.edu.uniandes.csw.mpusedvehicle.entities.ClientEntity;
import co.edu.uniandes.csw.mpusedvehicle.entities.ProductEntity;
import co.edu.uniandes.csw.mpusedvehicle.entities.ProviderEntity;
import co.edu.uniandes.csw.mpusedvehicle.entities.VehicleEntity;
import co.edu.uniandes.csw.mpusedvehicle.persistence.ProductPersistence;
import static co.edu.uniandes.csw.mpusedvehicle.tests._TestUtil.*;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class ProductPersistenceTest {
    public static final String DEPLOY = "Prueba";

    /**
     * @generated
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, DEPLOY + ".war")
                .addPackage(ProductEntity.class.getPackage())
                .addPackage(ProductPersistence.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * @generated
     */
    @Inject
    private ProductPersistence productPersistence;

    /**
     * @generated
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * @generated
     */
    @Inject
    UserTransaction utx;

    /**
     * @generated
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * @generated
     */
    private void clearData() {
        em.createQuery("delete from ProductEntity").executeUpdate();
    }

    /**
     * @generated
     */
    private List<ProductEntity> data = new ArrayList<ProductEntity>();
     /**
     * Cliente prueba
     */
    private ClientEntity clientEntity;
    /**
     * vehculo prueba
     */
    private VehicleEntity vehicleEntity;
    /**
     * vehculo prueba
     */
    private ProviderEntity providerEntity;
    /**
     * @generated
     */
    private void insertData() {
        vehicleEntity = new VehicleEntity();
            vehicleEntity.setName(generateRandom(String.class));
            vehicleEntity.setBrand(generateRandom(String.class));
            vehicleEntity.setModel(generateRandom(String.class));
            vehicleEntity.setColor(generateRandom(String.class));
            vehicleEntity.setCapacity(generateRandom(Integer.class));
        providerEntity = new ProviderEntity();
            providerEntity.setName(generateRandom(String.class));
            providerEntity.setUserId(generateRandom(String.class));    
        for (int i = 0; i < 3; i++) {
            ProductEntity entity = new ProductEntity();
            entity.setName(generateRandom(String.class));
            entity.setPrice(generateRandom(Integer.class));
            entity.setVehicle(vehicleEntity);
            em.persist(entity);
            data.add(entity);
        }
        providerEntity.setProducts(data);
        //Cliente de prueba
        clientEntity = new ClientEntity();
        clientEntity.setName(generateRandom(String.class));
        clientEntity.setUserId(generateRandom(String.class));
        em.persist(clientEntity);
        em.persist(vehicleEntity);
        em.persist(providerEntity);
    }

    /**
     * @generated
     */
    @Test
    public void createProductTest() {
        ProductEntity newEntity = new ProductEntity();
        newEntity.setName(generateRandom(String.class));
        newEntity.setPrice(generateRandom(Integer.class));

        ProductEntity result = productPersistence.create(newEntity);

        Assert.assertNotNull(result);

        ProductEntity entity = em.find(ProductEntity.class, result.getId());

        Assert.assertEquals(newEntity.getName(), entity.getName());
        Assert.assertEquals(newEntity.getPrice(), entity.getPrice());
    }

    /**
     * @generated
     */
    @Test
    public void getProductsTest() {
        List<ProductEntity> list = productPersistence.findAll(null, null);
        Assert.assertEquals(data.size(), list.size());
        for (ProductEntity ent : list) {
            boolean found = false;
            for (ProductEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * @generated
     */
    @Test
    public void getProductTest() {
        ProductEntity entity = data.get(0);
        ProductEntity newEntity = productPersistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getName(), newEntity.getName());
        Assert.assertEquals(entity.getPrice(), newEntity.getPrice());
    }

    /**
     * @generated
     */
    @Test
    public void deleteProductTest() {
        ProductEntity entity = data.get(0);
        productPersistence.delete(entity.getId());
        ProductEntity deleted = em.find(ProductEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    /**
     * @generated
     */
    @Test
    public void updateProductTest() {
        ProductEntity entity = data.get(0);

        ProductEntity newEntity = new ProductEntity();

        newEntity.setId(entity.getId());
        newEntity.setName(generateRandom(String.class));
        newEntity.setPrice(generateRandom(Integer.class));
        newEntity.setAmmountVotes(generateRandom(Integer.class));
        newEntity.setRating(generateRandom(Float.class));

        productPersistence.update(newEntity);

        ProductEntity resp = em.find(ProductEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getName(), resp.getName());
        Assert.assertEquals(newEntity.getPrice(), resp.getPrice());
    }

    /**
     * @generated
     */
    @Test
    public void getProductPaginationTest() {
        //Page 1
        List<ProductEntity> ent1 = productPersistence.findAll(1, 2);
        Assert.assertNotNull(ent1);
        Assert.assertEquals(2, ent1.size());
        //Page 2
        List<ProductEntity> ent2 = productPersistence.findAll(2, 2);
        Assert.assertNotNull(ent2);
        Assert.assertEquals(1, ent2.size());

        for (ProductEntity ent : ent1) {
            boolean found = false;
            for (ProductEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }

        for (ProductEntity ent : ent2) {
            boolean found = false;
            for (ProductEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * @generated
     */
    @Test
    public void findByName() {
        String name = data.get(0).getName();
        List<ProductEntity> cache = new ArrayList<ProductEntity>();
        List<ProductEntity> list = productPersistence.findByName(name);

        for (ProductEntity entity : data) {
            if (entity.getName().equals(name)) {
                cache.add(entity);
            }
        }
        Assert.assertEquals(list.size(), cache.size());
        for (ProductEntity ent : list) {
            boolean found = false;
            for (ProductEntity cacheEntity : cache) {
                if (cacheEntity.getName().equals(ent.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                Assert.fail();
            }
        }
    }
    
    /**
     * Prueba de updatePurchasedByClient y deletePurchasedByClient
     */
    @Test
    public void updatePurchasedByClientTest(){
        ProductEntity entity = data.get(0);
        
        productPersistence.updatePurchasedByClient(entity.getId(), clientEntity.getId());
        ProductEntity resp = em.find(ProductEntity.class, entity.getId());
        
        Assert.assertNotNull(resp);
        Assert.assertNotNull(resp.getPurchasedBy().get(0));
        Assert.assertEquals(clientEntity.getId(), resp.getPurchasedBy().get(0).getId());
        
        // Segunda parte
        productPersistence.deletePurchasedByClient(entity.getId(), clientEntity.getId());
        resp = em.find(ProductEntity.class, entity.getId());
        Assert.assertNotNull(resp);
        Assert.assertEquals(0,resp.getPurchasedBy().size());
    } 
    /**
     * Pruebas sobre getByVehicleName
     */
    @Test
    public void getByVehicleNameTest() {
        List<ProductEntity> newEntity = productPersistence.getByVehicleName(vehicleEntity.getName());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(3,newEntity.size());
        
    }
    /**
     * Pruebas sobre getCheaperProductByProvider
     */
    @Test
    public void getCheaperProductByProviderTest() {
        try{
            ProductEntity newEntity = productPersistence.getCheaperProductByProvider(providerEntity.getName());
        Assert.assertNotNull(newEntity);
        }catch(EJBException e){
            Assert.assertNotNull(e);
        } 
    }
    /**
     * Pruebas sobre getCheaperProductByVehicle
     */
    @Test
    public void getCheaperProductByVehicleTest() {
        try{
            ProductEntity newEntity = productPersistence.getCheaperProductByVehicle(providerEntity.getName());
        Assert.assertNotNull(newEntity);
        }catch(EJBException e){
            Assert.assertNotNull(e);
        } 
    }
    /**
     * Pruebas sobre getVehiclesName
     */
    @Test
    public void getVehiclesNameTest() {
        List<String> newEntity = productPersistence.getVehiclesName();
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(1,newEntity.size()); 
    }
    /**
     * Pruebas sobre getVehiclesBrand
     */
    @Test
    public void getVehiclesBrandTest() {
        List<String> newEntity = productPersistence.getVehiclesBrand();
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(1,newEntity.size()); 
    }
    /**
     * Pruebas sobre getVehiclesCapacity
     */
    @Test
    public void getVehiclesCapacityTest() {
        List<String> newEntity = productPersistence.getVehiclesCapacity();
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(1,newEntity.size()); 
    }
    /**
     * Pruebas sobre getVehiclesColor
     */
    @Test
    public void getVehiclesColorTest() {
        List<String> newEntity = productPersistence.getVehiclesColor();
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(1,newEntity.size()); 
    }
    /**
     * Pruebas sobre getVehiclesColor
     */
    @Test
    public void getVehiclesModelTest() {
        List<String> newEntity = productPersistence.getVehiclesModel();
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(1,newEntity.size()); 
    }
    /**
     * Pruebas sobre getVehiclesColor
     */
    @Test
    public void getVehiclesPlateTest() {
        List<String> newEntity = productPersistence.getVehiclesPlate();
        Assert.assertNotNull(newEntity);
    }
    /**
     * Pruebas sobre getVehiclesColor
     */
    @Test
    public void getVehiclesLocationTest() {
        List<String> newEntity = productPersistence.getVehiclesLocation();
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(1,newEntity.size()); 
    }
    /**
     * Pruebas sobre getProductsByAdvancedSearch
     */
    @Test
    public void getProductsByAdvancedSearchTest() {
        List<ProductEntity> newEntity = productPersistence.getProductsByAdvancedSearch(vehicleEntity.getBrand(), vehicleEntity.getModel(), vehicleEntity.getCapacity(), 0 ,vehicleEntity.getColor(), null, vehicleEntity.getLocation());
        Assert.assertNotNull(newEntity);
    }
    
}
