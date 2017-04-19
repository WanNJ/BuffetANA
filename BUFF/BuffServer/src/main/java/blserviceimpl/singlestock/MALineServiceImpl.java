package blserviceimpl.singlestock;

import blservice.singlestock.MALineService;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import util.DayMA;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public void setTest(DAOFactoryService factoryService) {
        factory = factoryService;
        stockDAO = factoryService.createStockDAO();
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

        List<MAPieceVO> maPieceVOs = new ArrayList<>();
        List<DayMA> list5 = getSingleCodeMAInfo( code,  beginDate,  endDate, 5);
        List<DayMA> list10 = getSingleCodeMAInfo( code,  beginDate,  endDate, 10);
        List<DayMA> list30 = getSingleCodeMAInfo( code,  beginDate,  endDate, 30);
        List<DayMA> list60 = getSingleCodeMAInfo( code,  beginDate,  endDate, 60);

        LocalDate date = beginDate;

        int ind5 = 0;
        int ind10 = 0;
        int ind30 = 0;
        int ind60 = 0;

        while(list5.size()>0&& list5.get(ind5).date.isBefore(beginDate)){
            ind5++;
        }
        while(list10.size()>0&& list10.get(ind10).date.isBefore(beginDate)){
            ind10++;
        }
        while(list30.size()>0&&list30.get(ind30).date.isBefore(beginDate)){
            ind30++;
        }
        while(list60.size()>0&&list60.get(ind60).date.isBefore(beginDate)){
            ind60++;
        }

        while(!date.isAfter(endDate)){
             MAPieceVO maPieceVO = new MAPieceVO();
             maPieceVO.date = date;

            if(list5.size()>0&&list5.get(ind5).date.equals(date)){
                maPieceVO.MA5 = list5.get(ind5).MAValue;
                ind5++;
            }
            if(list10.size()>0&&list10.get(ind10).date.equals(date)){
                maPieceVO.MA10 = list10.get(ind10).MAValue;
                ind10++;
            }
            if(list30.size()>0&&list30.get(ind30).date.equals(date)){
                maPieceVO.MA30 = list30.get(ind30).MAValue;
                ind30++;
            }
            if(list60.size()>0&&list60.get(ind60).date.equals(date)){
                maPieceVO.MA60 = list60.get(ind60).MAValue;
                ind60++;
            }
            maPieceVOs.add(maPieceVO);
            date = date.plusDays(1);

        }


        return maPieceVOs;
    }

    /**
     * 用地下写的均线方法替代
     * @param code
     * @param begin
     * @param end
     * @param days
     * @return
     */
    private  List<DayMA> getSingleCodeMAInfo(String code, LocalDate begin, LocalDate end, int days) {
        List<StockPO> list = stockDAO.getStockInfoByCode(code);
        list =  list.stream().filter(t->!t.getDate().isAfter(end)).collect(Collectors.toList());
        Collections.reverse(list);

        List<DayMA> ans = new ArrayList<>();

        //用于计算和
        double sum = 0;

        //用于计算算了几天的均线
        int js = 0;

        LocalDate temp = end;

        int none = 0;

        double ma = 0;
        int i;
        for ( i = 0 ; js < days  ;i++){
            if(i>list.size()-2*days){
                return new ArrayList<>();
            }

            if(list.get(i).getVolume()!=0){
                js++;
                sum+= list.get(i).getAdjCloseIndex();
            }else{
                none++;
            }


        }

        js = 0;
        for(;!temp.isBefore(begin);i++){
            if(i==list.size()-1)  new ArrayList<>();
            if(list.get(i).getVolume()!=0){
                ma = sum/days;
                while(list.get(js).getDate().isBefore(temp)){
                    ans.add(new DayMA(temp,ma));
                    temp = temp.minusDays(1);
                }
                ans.add(new DayMA(temp,ma));
                temp = temp.minusDays(1);
                sum+=list.get(i).getAdjCloseIndex()-list.get(js).getAdjCloseIndex();
                js++;
            }
        }
        Collections.reverse(ans);
        return ans;
    }
}
