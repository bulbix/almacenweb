dataSource {
	url = "jdbc:informix-sqli://192.168.10.10:1526/saihweb:informixserver=ol_inrserver"
	driverClassName = "com.informix.jdbc.IfxDriver"
	username = "informix"
	password = "informix"
	pooled = true	
	dialect = "org.hibernate.dialect.InformixDialect"
}

dataSource_materiales {
	url = "jdbc:informix-sqli://192.168.10.1:1526/almacenes:informixserver=ol_inrserver"
	driverClassName = "com.informix.jdbc.IfxDriver"
	username = "informix"
	password = "informix"
	pooled = true
	dialect = "org.hibernate.dialect.InformixDialect"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
	validator.apply_to_ddl = false
	validator.autoregister_listeners = false
}
// environment specific settings
environments {
    development {
        dataSource {
		   logSql = true
        }
		
		dataSource_materiales {			
			logSql = true
		}
    }
    test {
        dataSource {          
		   
        }
		
		dataSource_materiales {		
			
		}	
    }
    production {
        dataSource {			
			jndiName = "java:comp/env/jdbc/INR/Informix"           
        }
		
		dataSource_materiales {
			jndiName = "java:comp/env/jdbc/INR/sia"
		}
	}
}
