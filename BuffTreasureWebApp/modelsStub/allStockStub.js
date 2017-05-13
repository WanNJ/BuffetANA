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


};