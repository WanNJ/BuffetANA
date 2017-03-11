package blserviceimpl.singlestock;

import blservice.singlestock.MALineService;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public enum MALineServiceImpl implements MALineService {
    MA_LINE_SERVICE;


    private StockDAO stockDAO;
    private List<StockPO> stockPOs;
    private DAOFactoryService factory;
    private String code;

    MALineServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
        code = "";
    }

    /**
     * 用于传递stub
     * @param stockDAO
     */
    public void setDao(StockDAO stockDAO){
        this.stockDAO = stockDAO;
    }



    /**
     * //TODO  以后用lambada表达式替换
     * @param code 个股的编码
     * @param beginDate 均线图的开始日期
     * @param endDate 均线图的结束日期
     * @return List<MAPieceVO>
     * @throws RemoteException
     */
    @Override
    public List<MAPieceVO> getMAInfo(String code, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        if(!code.equals(this.code)){
            this.code = code;
            this.stockPOs = stockDAO.getStockInfoByCode(code);
        }
        System.out.println("stockPOs.size() : "+stockPOs.size());
        double sum5 = 0 , sum10 = 0, sum30 = 0,sum60 = 0;
        List<MAPieceVO> maPieceVOs = new ArrayList<>();
        for(int i = 0; i<stockPOs.size() && !stockPOs.get(i).getDate().isAfter(endDate);i++){
            StockPO stockPO  = stockPOs.get(i);
            //
            // System.out.println(stockPO.getVolume());
            if(stockPO.getVolume()!=0){
                sum5+= stockPO.getClose_Price();
                sum10+= stockPO.getClose_Price();
                sum30+= stockPO.getClose_Price();
                sum60+= stockPO.getClose_Price();

                if(i>=5)  sum5-= stockPOs.get(i-5).getClose_Price();
                if(i>=10)  sum10-= stockPOs.get(i-10).getClose_Price();
                if(i>=30)  sum30-= stockPOs.get(i-30).getClose_Price();
                if(i>=60)  sum60-= stockPOs.get(i-60).getClose_Price();

                if(!stockPO.getDate().isBefore(beginDate)){
                    MAPieceVO maPieceVO = new MAPieceVO();
                    maPieceVO.date =  stockPO.getDate();
                    maPieceVO.MA5 =  i>=5-1 ? sum5/5 : 0;
                    maPieceVO.MA10 =  i>=10-1 ? sum10/10 : 0;
                    maPieceVO.MA30 =  i>=30-1 ? sum30/30 : 0;
                    maPieceVO.MA60 =  i>=60-1 ? sum60/60 : 0;
                    maPieceVOs.add(maPieceVO);
                }
            }
        }

        return maPieceVOs;
    }
}
