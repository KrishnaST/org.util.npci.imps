/**
 * @author krishna.telgave
 */
module org.util.npci.imps {

	requires transitive org.util.iso8583;
	requires transitive org.util.npci.coreconnect;

	provides org.util.npci.coreconnect.issuer.IssuerDispatcherBuilder with org.util.npci.imps.IMPSDispatcherBuilder;

}