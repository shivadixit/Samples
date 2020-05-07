package com.sdixit.cloudos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.SocketOptions;

@Configuration
@PropertySources(value = {@PropertySource("classpath:application.properties")})
@EnableCassandraRepositories(basePackages= {"com.sdixit.cloudos"})
public class DBConfig extends AbstractCassandraConfiguration {

	private static final int CONNECTION_TIMEOUT_IN_MILLIS = 20000;
	private static final String DEFAULT_KEYSPACE_NAME = "cloudleaf"; // keyspace name has been renamed to cloudleaf in order to point the dev0-cluster.
	public static final String CASSANDRA_CONTACTPOINTS = "localhost";
	public static final String CASSANDRA_USERID = "cloudleaf";
	public static final String CASSANDRA_PASSWORD = "cloudleaf";

	 @Autowired
	  private Environment env;
	 	
	@Override
	public String getKeyspaceName() {
		return DEFAULT_KEYSPACE_NAME;
	}

	
	@Override
	public SocketOptions getSocketOptions() {
		SocketOptions o = new SocketOptions();
		o.setConnectTimeoutMillis(CONNECTION_TIMEOUT_IN_MILLIS);
		return o;
		
	}
	
	 @Override
	 public String getContactPoints() {
		 return CASSANDRA_CONTACTPOINTS;
	 }

	@Override
	protected QueryOptions getQueryOptions() {
	    QueryOptions queryOptions = new QueryOptions();
	    queryOptions.setConsistencyLevel(ConsistencyLevel.ONE);
	    return queryOptions;
	}


	protected AuthProvider getAuthProvider() {
		String username = CASSANDRA_USERID;
		String password = CASSANDRA_PASSWORD;
		return new PlainTextAuthProvider(username, password);
	}

}
