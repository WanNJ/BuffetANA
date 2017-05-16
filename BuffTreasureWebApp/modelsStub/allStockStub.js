/**
 * Created by wshwbluebird on 2017/5/13.
 */


exports.allStockDB = {

    /**
     * 获得所有股票的代码和名称
     * @param callback
     */
    getAllStockCodeAndName: (callback) => {
        callback(null,[['100001','qww'],['100002','qww'],['100003','qww']]);
    },

    /**
     * 获得沪深300所有股票代码和名称
     * @param callback
     */
    getHS300StockCodeAndName: (callback) => {
        callback(null,[['100001','wer'],['100002','qww'],['100003','qww']]);
    },

    /**
     * 获得同行业的所有股票的代码和名称
     * @param industry
     * @param callback
     */
    getStocksByIndustry: (industry, callback) => {
        callback(null,[['100005','wer'],['100002','qww'],['100004','eee']]);
    },

    /**
     * 获得属于某些板块的所有股票的代码和名称
     * @param bench 板块的列表，如果只有一个板块，则是只包含一个元素的列表
     * @param callback
     */
    getStocksByBench: (bench, callback) => {
        callback(null,[['100001',''],['100002','qww'],['100003','qww']]);
    },

    /**
     * 根据所给的股票代号获得这支股票所在的行业
     * ！！！一支股票只有一个行业！！！！
     * @param code
     * @param callback
     */
    getIndustryByCode: (code, callback) => {
        callback(null,[['100001','eee'],['100002','eee'],['100003','eee']]);
    },

    /**
     * 根据所给的股票代号获得这支股票所在的板块
     * ！！！一支股票可能从属于多个板块，所以所属板块是一个数组！！！！
     * @param code
     * @param callback
     */
    getBoardsByCode: (code, callback) => {
        callback(null,[['100001','qww'],['100002','qww'],['100003','qww']]);
    },

    /**
     * 获得所有行业
     * @param callback
     */
    getAllIndustry: (callback) => {
        callback(null,[['100001','eee'],['100002','qww'],['100003','qww']]);
    },

    /**
     * 获得所有板块
     * @param callback
     */
    getAllBoards: (callback) => {
        callback(null,[['100001','eee'],['100002','eee'],['100003','eee']]);
    },


    /**
     * 获得随机(目前就是前500只股票的代码和名称
     * @param callback
     */
    getRandom500StockCodeAndName: (callback) => {
        callback(null,[['100001','qwe'],['100002','qwe'],['100003','qwe']]);
    },

    /**
     * 获得自定义股票池的股票代码和名称
     * @param industries {Array}
     * @param boards {Array}
     * @param callback
     */
    getDIYStockPool: (industries, boards, callback) => {
        callback(null,[['100001','*STdsfdsf'],['100002','dsfdsf'],['100003','qwe']]);
    },


    /**
     * 获得中小板所有股票代码和名称
     * @param callback
     */
    getSMEBoardCodeAndName: (callback) => {
        callback(null,[
            {'code':'000001','name':'中国平安'}
        ,{'code':'000002','name':'中国平安'}
        ,{'code':'000014','name':'中国平安'}
         ,{'code':'000544','name':'中国平安'},{'code':'000548','name':'中国平安'}
            ,{'code':'000006','name':'中国平安'},{'code':'000007','name':'中国平安'}
            ,{'code':'000008','name':'中国平安'},{'code':'000009','name':'中国平安'}
            ,{'code':'000010','name':'中国平安'},{'code':'000545','name':'中国平安'}
            ,{'code':'000014','name':'中国平安'},{'code':'00016','name':'中国平安'}
            ,{'code':'000544','name':'中国平安'},{'code':'000548','name':'中国平安'}
            ,{'code':'000151','name':'中国平安'},{'code':'000153','name':'中国平安'}
            ,{'code':'000157','name':'中国平安'},{'code':'000156','name':'中国平安'}
            ,{'code':'000301','name':'中国平安'},{'code':'000400','name':'中国平安'}
            ,{'code':'000333','name':'中国平安'},{'code':'000338','name':'中国平安'}

        ]);
    },


    getGEMBoardStockCodeAndName: (callback) => {
        callback(null,[
            {'code':'000001','name':'中国平安'}
            ,{'code':'000002','name':'中国平安'}
            ,{'code':'000014','name':'中国平安'}
            ,{'code':'000544','name':'中国平安'}
            ,{'code':'000548','name':'中国平安'}
            ,{'code':'000006','name':'中国平安'},{'code':'000007','name':'中国平安'}
            ,{'code':'000008','name':'中国平安'},{'code':'000009','name':'中国平安'}
            ,{'code':'000010','name':'中国平安'},{'code':'000545','name':'中国平安'}
            ,{'code':'000014','name':'中国平安'},{'code':'000016','name':'中国平安'}
            ,{'code':'000544','name':'中国平安'},{'code':'000548','name':'中国平安'}
            ,{'code':'000151','name':'中国平安'},{'code':'000153','name':'中国平安'}
            ,{'code':'000157','name':'中国平安'},{'code':'000156','name':'中国平安'}
            ,{'code':'000301','name':'中国平安'},{'code':'000400','name':'中国平安'}
            ,{'code':'000333','name':'中国平安'},{'code':'000338','name':'中国平安'}

        ]);
    },

};