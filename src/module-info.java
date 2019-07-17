/**
 * @author krishna.telgave
 */
module org.util.npci.imps {

	requires transitive java.sql;
	
	requires transitive org.util.iso8583;
	requires transitive org.util.iso8583.npci;
	requires transitive org.util.npci.coreconnect;
	
	requires transitive retrofit2;
	requires transitive okhttp3;
	requires transitive retrofit2.converter.jackson;
	
	requires transitive com.fasterxml.jackson.core;
	requires transitive com.fasterxml.jackson.databind;
	requires transitive com.fasterxml.jackson.annotation;

	uses org.util.npci.imps.db.DatabaseServiceBuilder;
	uses org.util.npci.imps.cbs.CoreBankingServiceBuilder;
	
	provides org.util.npci.coreconnect.issuer.IssuerDispatcherBuilder with org.util.npci.imps.IMPSDispatcherBuilder;
	provides org.util.npci.imps.db.DatabaseServiceBuilder with org.util.npci.imps.db.InternalDatabaseServiceBuilder;

}