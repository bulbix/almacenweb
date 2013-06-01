dataSource {
	url = "jdbc:informix-sqli://192.168.10.10:1526/saihweb:informixserver=ol_inrserver"
	driverClassName = "com.informix.jdbc.IfxDriver"
	username = "informix"
	password = "informix"
	pooled = true	
	dialect = "org.hibernate.dialect.InformixDialect"
}

dataSource_materiales {
	url = "jdbc:informix-sqli://192.168.10.1:1526/mat_2011:informixserver=ol_inrserver"
	driverClassName = "com.informix.jdbc.IfxDriver"
	username = "informix"
	password = "informix"
	pooled = true
	dialect = "org.hibernate.dialect.InformixDialect"
}


hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
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
		   logSql = true
        }
		
		dataSource_materiales {			
			logSql = true
		}	
    }
    production {
        dataSource {
            //dbCreate = "update"
            url = "jdbc:informix-sqli://192.168.10.10:1526/saihweb:informixserver=ol_inrserver"           
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="select first 1 * from systables"
            }
        }
		
		dataSource_materiales {
			//dbCreate = "update"
			url = "jdbc:informix-sqli://192.168.10.1:1526/mat_2011:informixserver=ol_inrserver"			
			properties {
			   maxActive = -1
			   minEvictableIdleTimeMillis=1800000
			   timeBetweenEvictionRunsMillis=1800000
			   numTestsPerEvictionRun=3
			   testOnBorrow=true
			   testWhileIdle=true
			   testOnReturn=true
			   validationQuery="select first 1 * from systables"
			}
		}
		
		
		
		
    }
}
