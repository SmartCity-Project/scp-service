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
public class LongToObjectIdConverter 
	implements Converter<Long, ObjectId>
{

	@Override
	public ObjectId convert(Long source) {
		return (source == null)? null : new ObjectId(BigInteger.valueOf(source).toString());
	}

}
