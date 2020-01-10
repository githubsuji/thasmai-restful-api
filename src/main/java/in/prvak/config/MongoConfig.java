package in.prvak.config;
//package com.calibroz.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.net.SocketFactory;
//import javax.net.ssl.SSLContext;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.MongoClientURI;
//import com.mongodb.MongoCredential;
//import com.mongodb.ReadPreference;
//import com.mongodb.ServerAddress;
//import com.mongodb.WriteConcern;
//
//@Configuration
//@EnableMongoRepositories(basePackages="repository")
//public class MongoConfig extends AbstractMongoConfiguration {
//	
//	
//	Logger logger = LoggerFactory.getLogger(MongoConfig.class);
//	@Value("${mongodb.sslEnabled}")
//	private boolean sslEnabled;
//
//	@Value("${mongodb.datasource.url}")
//	private String mongoClientUrl;
//
//	@Value("${mongodb.database}")
//	private String mongoDB;
//
//	@Value("${mongodb.username}")
//	private String username;
//
//	@Value("${mongodb.password}")
//	private String password;
//
//	@Value("${mongodb.hostname}")
//	private String hostname;
//
//	@Value("${mongodb.port}")
//	private int port;
//  
////    @Override
////    protected String getDatabaseName() {
////        return "test";
////    }
//  
////    @Bean
////    @Override
////    public MongoClient mongoClient() {
////        return new MongoClient("localhost", 27017);
////    }
//    
//    @Primary
//	@Bean()
//    @Override    
//	public MongoClient mongoClient()  {
//		String servers[] = hostname.split(",");
//		List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
//		for (int i = 0; i < servers.length; i++) {
//			serverAddresses.add(new ServerAddress(servers[i], port));
//		}
//
//		if (sslEnabled) {
//			MongoCredential creds = MongoCredential.createCredential(username, mongoDB, password.toCharArray());
//
//			return new MongoClient(serverAddresses, creds,
//					new MongoClientOptions.Builder().connectTimeout(180000).minConnectionsPerHost(0)
//							.connectionsPerHost(25).threadsAllowedToBlockForConnectionMultiplier(5).maxWaitTime(120000)
//							.maxConnectionIdleTime(60000).maxConnectionLifeTime(0).socketTimeout(60000)
//							.socketKeepAlive(true).readPreference(ReadPreference.primary())
//							.writeConcern(new WriteConcern(Integer.valueOf(1), Integer.valueOf(1000000)))
//							.sslEnabled(true).socketFactory(getSocketContextFactory("true")).build());
//		} else {
//			return new MongoClient(new MongoClientURI(mongoClientUrl));
//		}
//
//	}
//    
//	private SocketFactory getSocketContextFactory(String sslEnabled) {
//		SSLContext sslContext;
//		if (null != sslEnabled && Boolean.valueOf(sslEnabled)) {
//			try {
//				sslContext = SSLContext.getInstance("TLSv1.2");
//				sslContext.init(null, null, null);
//				return sslContext.getSocketFactory();
//			} catch (Exception exception) {
//				logger.error("Exception while setting SSL", exception);
//			}
//		}
//		return SocketFactory.getDefault();
//	}
//
//	@Override
//	protected String getDatabaseName() {
//		return mongoDB;
//	}
//}