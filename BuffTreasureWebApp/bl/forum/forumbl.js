/**
 * Created by wshwbluebird on 2017/6/8.
 */


let contentDB = require('../../models/content').contentDB;
let forumDB = require('../../models/forum').forumDB;
const userDB = require('../../models/user').userDB;
const allStockDB = require('../../models/allstock').allStockDB;

exports.forumbl = {

    /**
     * 某一客户对某一支股票点赞的动作
     * @param code  股票代码
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）={}
     */
    pressStockGood:(code,userID,callback)=>{
        forumDB.pressGood(code,userID,(err)=>{
            callback(err)
        })
    },

    /**
     * 某一客户对某一支股票点差的动作
     * @param code  股票代码
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）={}
     */
    pressStockBad:(code,userID,callback)=>{
        forumDB.pressBad(code,userID,(err)=>{
            callback(err)
        })
    },

    /**
     * 某一客户对某一个评论点赞的动作
     * @param content_id  评论的ID
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）= {}
     */
    pressContentGood:(content_id,userID,callback)=>{
        contentDB.pressGood(content_id,userID,(err, stockCode)=>{
            allStockDB.getNameByCode(stockCode, (err, name) => {
                contentDB.getSenderByID(content_id, (err, userName) => {
                    let message = {};
                    if (err) {
                        message = {
                            time: new Date(),
                            isRead: false,
                            type: 'error',
                            codeOrName: '点赞出错',
                            stockName: name["name"],
                            content: {
                                content_id: content_id,
                                userID: userID
                            }
                        }
                    }
                    else {
                        message = {
                            time: new Date(),
                            isRead: false,
                            type: 'thumbs_up',
                            codeOrName: stockCode,
                            stockName: name["name"],
                            content: {
                                content_id: content_id,
                                userID: userID
                            }
                        }
                    }
                    userDB.addUnreadMessage(userName, message, (err) => {
                        callback(err);
                    });

                });
            });
        });
    },

    /**
     * 某一客户对某一支股票点差的动作
     * @param content_id  评论的ID
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）={}
     */
    pressContentBad:(content_id,userID,callback)=>{
        contentDB.pressBad(content_id,userID,(err, stockCode)=>{
            allStockDB.getNameByCode(stockCode, (err, name) => {
                contentDB.getSenderByID(content_id, (err, userName) => {
                    let message = {};
                    if (err) {
                        message = {
                            time: new Date(),
                            isRead: false,
                            type: 'error',
                            codeOrName: '点踩出错',
                            stockName: name["name"],
                            content: {
                                content_id: content_id,
                                userID: userID
                            }
                        }
                    }
                    else {
                        message = {
                            time: new Date(),
                            isRead: false,
                            type: 'thumbs_down',
                            codeOrName: stockCode,
                            stockName: name["name"],
                            content: {
                                content_id: content_id,
                                userID: userID
                            }
                        }
                    }
                    userDB.addUnreadMessage(userName, message, (err) => {
                        callback(err);
                    });
                });
            });
        });
    },

    /**
     * 某一客户对某一支股票评论
     * @param code  股票代码
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param content 评论的内容
     * @param callback 形如 （err）={}
     */
    commentStock: (code,userID,content,callback)=>{
        contentDB.addContent(code,userID,content,(err)=>{
            callback(err)
        });
    },

    /**
     * 返回用户在特定股票的论坛中可以看到的东西
     * @param code 股票代码
     * @param userID 点赞用户的 用户名(用户名和id 是一个东西)
     * @param callBack    形如 （err,doc）={}
     * doc {JSON}
     * 格式如下
     * {
     *  "like":{Number} 点赞的人数
     *  "dislike":{Number}  点差的人数
     *  "clickAble":{Bool} 可否点赞或者点差 true 可以
     *   "contents":[]
     * }
     *
     * contents{JSON}
     * 内容如下
     * {
     *  "like":{Number} 点赞的人数
     *  "dislike":{Number}  点差的人数
     *  "date":{Date} 评论的时间
     *  "clickAble":{Bool} 可否对这条评论点赞或者点差 true 可以
     *  "content":{String}
     *  "username ":{String}
     *  "_id" :{String}
     * }
     *
     */
    getAllStockComment:(code,userID,callBack)=>{
        forumDB.getForumInfo(code,userID,(err,forum)=>{
            contentDB.getAllContent(code,userID,(err,content)=>{

                forum['contents'] = content
                callBack(err,forum)
            })

        })
    }
};