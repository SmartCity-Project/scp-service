/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.repositories.IRepository;
import com.smartcity.business.rest.IBaseCrudRestController;
import com.smartcity.data.common.Article;

/**
 * @author gperreas
 *
 */
@RestController
//@RequestMapping(value="/api/crud/articles")
public class ArticleRestController
	extends BaseRestController
	implements IBaseCrudRestController<Article, Long>
{
	private Logger log = LoggerFactory.getLogger(ArticleRestController.class);

	@Override
	public Map<String, Object> save(Article json) {

		return null;
	}


	@Override
	public Map<String, Object> edit(Article json) {

		return null;
	}


	@Override
	public boolean delete(Long id) {

		return false;
	}


}
