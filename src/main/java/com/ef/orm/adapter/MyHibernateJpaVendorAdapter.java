package com.ef.orm.adapter;

import java.util.Map;

import org.hibernate.cfg.Environment;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Force re-creation of DB schema.
 * 
 * @author owenrb
 *
 */
public class MyHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter{

	/* (non-Javadoc)
	 * @see org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter#getJpaPropertyMap()
	 */
	@Override
	public Map<String, Object> getJpaPropertyMap() {
		Map<String, Object> jpaProperties =  super.getJpaPropertyMap();
		

		if (isGenerateDdl()) {
			jpaProperties.put(Environment.HBM2DDL_AUTO, "create"); // force re-creation of DB schema.
		}
		
		return jpaProperties;
	}

}
