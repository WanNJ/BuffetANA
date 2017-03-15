package driver;

import gui.utils.LocalHistoryService;

/**
 * Created by wshwbluebird on 2017/3/15.
 */
public class testHistory {
    public static void main(String[] args){
        LocalHistoryService.LOCAL_HISTORY_SERVICE.addHistoryPiece("12x");
        LocalHistoryService.LOCAL_HISTORY_SERVICE.getList().stream().forEach(t-> System.out.println(t));
}
}
