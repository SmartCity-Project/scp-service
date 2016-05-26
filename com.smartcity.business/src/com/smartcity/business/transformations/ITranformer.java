/**
 * 
 */
package com.smartcity.business.transformations;

import java.util.List;

import com.smartcity.exceptions.EntityNotFoundException;


/**
 * Common DTO and Domain transformer interface
 * 
 * @author gperreas
 *
 */
public interface ITranformer<Entity, DTO> {

	Entity transformToEntity(DTO dto) throws EntityNotFoundException;

    DTO transformToDTO(Entity entity);

    List<Entity> transformToEntities(List<DTO> dtoList) throws EntityNotFoundException;

    List<DTO> transformToDTOs(List<Entity> entityList);
}
