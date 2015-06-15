dataSource {
	pooled = true
    driverClassName = "org.postgresql.Driver"
    dialect = "org.hibernate.dialect.PostgreSQLDialect"
    username = "postgres"
    password = "garbage"
}

dataSource_materiales {
	pooled = true
    driverClassName = "org.postgresql.Driver"
    dialect = "org.hibernate.dialect.PostgreSQLDialect"
    username = "postgres"
    password = "garbage"
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
			dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
			url = "jdbc:postgresql://localhost:5432/almacenes"
		   logSql = true
        }
		
		dataSource_materiales {			
			logSql = true
		}
    }
    test {
        dataSource {          
			dbCreate = "update"
			url = "jdbc:postgresql://localhost:5432/almacenes"
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
