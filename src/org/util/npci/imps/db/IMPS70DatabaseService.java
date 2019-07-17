package org.util.npci.imps.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.util.nanolog.Logger;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.AccountDetails;

public class IMPS70DatabaseService extends DatabaseService {

	public IMPS70DatabaseService(final CoreConfig config, final IMPSDispatcher dispatcher) {
		super(config, dispatcher);
	}

	@Override
	public final String getName() {
		return "IMPS70";
	}

	@Override
	public final AccountDetails getAccountDetails(String mobile, String mmid, Logger logger) {
		try(Connection con = config.dataSource.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT ACCTNO, ACCTNO15DIGIT, BRCODE FROM ACCOUNTS WHERE MOBILENO like ? AND MMID = ?")) {
			ps.setString(1, "%"+mobile+"%");
			ps.setString(2, mmid);
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					return new AccountDetails(rs.getString("ACCTNO"), rs.getString("ACCTNO15DIGIT"), rs.getInt("BRCODE"));
				}
			} catch (Exception e) {logger.error(e);}
			
		} catch (Exception e) {logger.error(e);}
		return null;
	}

}
