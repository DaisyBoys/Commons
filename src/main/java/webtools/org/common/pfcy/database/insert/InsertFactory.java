package webtools.org.common.pfcy.database.insert;

import webtools.common.database.JdbcAgent;

/**
 * ���봦�?��
 * @author jack.dong
 *
 */
public class InsertFactory {

	public boolean execute(String[] sqls) throws Exception {
		boolean result = false;

		JdbcAgent agent = new JdbcAgent();
		if (sqls != null) {
			result = agent.batchupdate(sqls);
		}

		return result;
	}

}
