/**
 * Created by wshwbluebird on 2017/6/8.
 */


let contentDB = require('../../models/content').contentDB;
let contentDB = require('../../models/content').contentDB;

exports.forumbl = {

    /**
     * 某一客户对某一支股票点赞的动作
     * @param code  股票代码
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）={}
     */
    pressStockGood:(code,userID,callback)=>{

    },

    /**
     * 某一客户对某一支股票点差的动作
     * @param code  股票代码
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）={}
     */
    pressStockBad:(code,userID,callback)=>{

    },

    /**
     * 某一客户对某一个评论点赞的动作
     * @param content_id  评论的ID
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）={}
     */
    pressContentGood:(content_id,userID,callback)=>{

    },

    /**
     * 某一客户对某一支股票点差的动作
     * @param content_id  评论的ID
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param callback 形如 （err）={}
     */
    pressContentBad:(content_id,userID,callback)=>{

    },

    /**
     * 某一客户对某一支股票评论
     * @param code  股票代码
     * @param userID  点赞用户的 用户名(用户名和id 是一个东西)
     * @param content 评论的内容
     * @param callback 形如 （err）={}
     */
    commentStock: (code,userID,content,callback)=>{

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
     * }
     *
     */
    getAllStockComment:(code,userID,callBack)=>{

    }
}