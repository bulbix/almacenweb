dataSource {
	url = "jdbc:informix-sqli://192.168.10.10:1526/saihweb:informixserver=ol_inrserver"
	driverClassName = "com.informix.jdbc.IfxDriver"
	username = "informix"
	password = "informix"
	pooled = true
	logSql = true
	//dialect = "hibernate.Informix10Dialect"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
           url = "jdbc:informix-sqli://192.168.10.10:1526/saihweb:informixserver=ol_inrserver"
		   driverClassName = "com.informix.jdbc.IfxDriver"
		   username = "informix"
		   password = "informix"
		   pooled = true
		   logSql = true
        }
		
		dataSource_materiales {
			url = "jdbc:informix-sqli://192.168.10.1:1526/mat_2011:informixserver=ol_inrserver"
			driverClassName = "com.informix.jdbc.IfxDriver"
			username = "informix"
			password = "informix"
			pooled = true
			logSql = true
		}
    }
    test {
         dataSource {
           url = "jdbc:informix-sqli://192.168.10.10:1526/saihweb:informixserver=ol_inrserver"
		   driverClassName = "com.informix.jdbc.IfxDriver"
		   username = "informix"
		   password = "informix"
		   pooled = true
		   logSql = true
        }
		
		dataSource_materiales {
			url = "jdbc:informix-sqli://192.168.10.1:1526/mat_2011:informixserver=ol_inrserver"
			driverClassName = "com.informix.jdbc.IfxDriver"
			username = "informix"
			password = "informix"
			pooled = true
		}	
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
