/**
 * 
 */
package com.smartcity.business.transformations;

import java.util.ArrayList;
import java.util.List;

import com.smartcity.exceptions.EntityNotFoundException;

/**
 * @author gperreas
 *
 */
public abstract class BaseTransformer<Entity, DTO>
	implements ITranformer<Entity, DTO>
{

	@Override
	public List<Entity> transformToEntities(List<DTO> dtoList) throws EntityNotFoundException {
		List<Entity> entityList = new ArrayList<Entity>();
        for (DTO dto : dtoList) {
            entityList.add(transformToEntity(dto));
        }
        return entityList;
	}

	@Override
	public List<DTO> transformToDTOs(List<Entity> entityList) {
		List<DTO> dtoList = new ArrayList<DTO>();
        for (Entity entity : entityList) {
            dtoList.add(transformToDTO(entity));
        }
        return dtoList;
	}

}
