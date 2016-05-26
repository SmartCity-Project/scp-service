/**
 * 
 */
package com.smartcity.business.converters;

import java.math.BigInteger;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

/**
 * @author gperreas
 *
 */
public class ObjectIdToLongConverter 
	implements Converter<ObjectId, Long>
{

	@Override
	public Long convert(ObjectId source) {
        return (source == null)? null : new BigInteger(source.toString(), 16).longValue();
    }

}
