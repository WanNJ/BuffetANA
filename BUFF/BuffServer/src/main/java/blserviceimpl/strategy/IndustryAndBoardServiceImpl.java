package blserviceimpl.strategy;

import blservice.strategy.IndustryAndBoardService;
import dataservice.industry.IndustryDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/4/14.
 */
public enum IndustryAndBoardServiceImpl implements IndustryAndBoardService {
    INDUSTRY_AND_BOARD_SERVICE;

    private IndustryDAO industryDAO;
    private DAOFactoryService factory;
    private List<String> industries;

    IndustryAndBoardServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        industryDAO = factory.createIndustryDAO();
        industries = industryDAO.getIndustry();
    }

    @Override
    public List<String> getAllIndustries() {
        return industries;
    }

    @Override
    public List<String> getAllBoards() {
        List<String> boards = new ArrayList<>();
        boards.add("主板");
        boards.add("中小板");
        boards.add("创业板");
        return boards;
    }
}
