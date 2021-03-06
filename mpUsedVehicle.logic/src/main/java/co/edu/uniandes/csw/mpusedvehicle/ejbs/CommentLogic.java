package co.edu.uniandes.csw.mpusedvehicle.ejbs;

import co.edu.uniandes.csw.mpusedvehicle.api.ICommentLogic;
import co.edu.uniandes.csw.mpusedvehicle.converters.CommentConverter;
import co.edu.uniandes.csw.mpusedvehicle.dtos.CommentDTO;
import co.edu.uniandes.csw.mpusedvehicle.entities.CommentEntity;
import co.edu.uniandes.csw.mpusedvehicle.persistence.CommentPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @generated
 */
@Stateless
public class CommentLogic implements ICommentLogic {

    @Inject
    private CommentPersistence persistence;

    /**
     * @generated @return
     */
    @Override
    public int countComments() {
        return persistence.count();
    }

    /**
     * @generated @return
     */
    @Override
    public List<CommentDTO> getComments(Integer page, Integer maxRecords) {
        return CommentConverter.listEntity2DTO(persistence.findAll(page, maxRecords));
    }

    /**
     * @generated @return
     */
    @Override
    public CommentDTO getComment(Long id) {
        return CommentConverter.fullEntity2DTO(persistence.find(id));
    }

    /**
     * @generated @return
     */
    @Override
    public CommentDTO createComment(CommentDTO dto) {
        CommentEntity entity = CommentConverter.fullDTO2Entity(dto);
        persistence.create(entity);
        return CommentConverter.fullEntity2DTO(entity);
    }

    /**
     * @generated @return
     */
    @Override
    public CommentDTO updateComment(CommentDTO dto) {
        CommentEntity entity = persistence.update(CommentConverter.fullDTO2Entity(dto));
        return CommentConverter.fullEntity2DTO(entity);
    }

    /**
     * @generated @return
     */
    @Override
    public void deleteComment(Long id) {
        persistence.delete(id);
    }

    /**
     *
     * @param productId
     * @return
     */
    @Override
    public List<CommentDTO> getByProduct(Long productId) {
        return CommentConverter.listEntity2DTO(persistence.getByProduct(productId));
    }
}
