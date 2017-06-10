/**
 * Created by slow_time on 2017/6/10.
 */
let hotBoard = require('./hotStockAndBoardSchema');

exports.hotDB = {

    /**
     * 获得热门板块
     * @param callback
     */
    getHotBoards: (callback) => {
        hotBoard.findOne({now: 1}, ['hot'], (err, hotBoards) => {
            if (err)
                callback(err, null);
            else
                callback(null, hotBoards);
        });
    },

    /**
     * 更新热门板块
     * @param hotBoards
     * @param callback
     */
    updateHotBoards: (hotBoards, callback) => {
        hotBoard.updateOne({now: 1}, {$set: {hot: hotBoards}}, (err) => {
            if (err)
                callback(err, false);
            else
                callback(null, true);
        });
    },


    /**
     * 获得热门股票
     * @param callback
     */
    getHotStocks: (callback) => {
        hotBoard.findOne({now: 2}, ['hot'], (err, hotBoards) => {
            if (err)
                callback(err, null);
            else
                callback(null, hotBoards);
        });
    },


    /**
     * 更新热门股票
     * @param hotStocks
     * @param callback
     */
    updateHotStocks: (hotStocks, callback) => {
        hotBoard.updateOne({now: 2}, {$set: {hot: hotStocks}}, (err) => {
            if (err)
                callback(err, false);
            else
                callback(null, true);
        });
    }
};