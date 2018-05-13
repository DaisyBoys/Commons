package webtools.HTTPJSON.tools;

import webtools.HTTPJSON.GsonMapMgr;
import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库通用方法管理
 */
public final class RSPageMgr {
    private JdbcAgent jAgent = new JdbcAgent();
    private GsonMapMgr pGsonMapMgr = new GsonMapMgr();//Gson管理类
    private ListMgr pListMgr = new ListMgr();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            jAgent = null;
            pGsonMapMgr = null;
            pListMgr = null;
        } catch (Exception e) {

        }


    }

    //当前页
    private int nCurrPage = 0;

    public int getNCurrPage() {
        return nCurrPage;
    }

    public void setNCurrPage(int currPage) {
        nCurrPage = currPage;
    }

    //记录总数
    private int nPageSize = 10;

    public int getNPageSize() {
        return nPageSize;
    }

    public void setNPageSize(int pageSize) {
        nPageSize = pageSize;
    }

    //总记录数
    private int nTotal = 0;

    public int getNTotal() {
        return nTotal;
    }

    public void setNTotal(int total) {
        nTotal = total;
    }

    //总页数
    private int nPageCnt = 0;

    public int getNPageCnt() {
        return nPageCnt;
    }

    public void setNPageCnt(int pageCnt) {
        nPageCnt = pageCnt;
    }

    /**
     * 查询处理
     *
     * @param sql=查询语句，from=数据库FROM ，where=数据WHERE,mapParam=传入的参数
     */
    public DBResult query(String sql, String from, String where, Map<String, Object> mapParam) {
        DBResult rs = null;
        try {
            //得到当前页
            this.nCurrPage = this.getCurrentPageNo(mapParam);
            //得到记录总数
            int nTotal = this.getRSTotal(from, where);
            //得到当前页数
            int nPageCnt = this.GetTotalPageCnt(nTotal, nPageSize);
            if (nCurrPage >= nPageCnt) nPageCnt--;
            if (nPageCnt <= 0) nPageCnt = 1;
            //得到记录查询分页起始于终止位置
            int nPos1 = nCurrPage * nPageSize;
            int nPos2 = (nCurrPage + 1) * nPageSize - 1;
            //得到记录查询结果
            rs = jAgent.query(sql, nPos1, nPos2);
            this.setNTotal(nTotal);//总记录数
            this.setNPageCnt(nPageCnt);//总分页数量
            this.setNPageSize(nPageSize);//每页记录数量
            this.setNCurrPage(nCurrPage);//当前分页页号

        } catch (Exception e) {
            // TODO: handle exception
        }
        return rs;
    }

    /**
     * 创建通用的数据返回结果
     */
    public Map<String, Object> createRSMapData(final DBResult rs, final int iRow) {
        Map<String, Object> pData = new ConcurrentHashMap<String, Object>();
        try {

            for (int j = 0; j < rs.getColCount(); j++) {
                String colName = rs.getColumAttribute(j).getColname();//得到列名
                String colType = rs.getColumAttribute(j).getType().toString();
                if ("VARBINARY".equals(colType)) {
                    Object s = rs.getObject(iRow, j);
                }

                String srtVal = rs.getString(iRow, j);
                String str = (String) pData.get(colName);
                if (colType.equalsIgnoreCase("DATETIME")) {
                    if (srtVal.length() > 19) srtVal = srtVal.substring(0, 19);
                }
                if (str == null) {
                    pData.put(colName, srtVal);
                } else {
                    pData.put(colName + j, srtVal);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return pData;
    }

    /**
     * 根据输入参数得到当前页
     */
    public int getCurrentPageNo(Map<String, Object> mapParam) {
        String currpage = pGsonMapMgr.getString(mapParam, "currpage");
        this.nCurrPage = pListMgr.convertInt(currpage);
        if (this.nCurrPage <= 0) this.nCurrPage = 1;
        this.nCurrPage--;//真实使用的当前页数
        if (this.nCurrPage < 0) nCurrPage = 0;
        return this.nCurrPage;
    }

    /**
     * 得到当前所有页数
     */
    public int GetTotalPageCnt(int nTotal, int nPageSize) {
        if (nPageSize == 0) return 0;
        int nPage = nTotal / nPageSize;
        int nModPage = nTotal % nPageSize;
        if (nModPage > 0) nPage++;
        return nPage;
    }

    /**
     * 得到记录总数
     */
    public int getRSTotal(String from, String where) {
        int nTotal = 0;
        try {
            String sql = "select count(*) " + from + where + " ";
            DBResult result = jAgent.query(sql);
            //System.out.println(sqlCnt);

            if (result.getRowCount() > 0) {
                if (result.getRowCount() > 1) {
                    nTotal = result.getRowCount();
                } else {
                    String sTotal = result.getString(0, 0);//记录集总数
                    try {
                        nTotal = Integer.parseInt(sTotal);
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return nTotal;
    }
}
