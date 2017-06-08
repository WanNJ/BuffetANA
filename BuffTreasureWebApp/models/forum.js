/**
 * Created by wshwbluebird on 2017/5/28.
 */
let forumSchema = require('./forumSchema');

exports.forumDB ={
    /**
     * 点赞／看好
     * @param code 股票代码
     * @param userID 用户的ID
     * @param callback
     */
    pressGood :(code,userID,callback)=>{
        forumSchema.findOne({'code':code},'good',(err, data) => {
            if(err||data===null) {
                let forumCode = {
                    'code': code,
                    'good': [userID],
                    'bad': [],
                    'content_id': []
                }
                newSchema = new forumSchema(forumCode)
                newSchema.save((err, doc) => {
                    callback(err, doc)
                })
            }
            else{
                goodList = data['good']
                goodList.push(userID)
                forumSchema.update({'code':code},{$set: {"good": goodList}},(err,doc)=>{
                    callback(err,doc)
                })
            }

        });
    },

    /**
     * 点差／不看好
     * @param code 股票代码
     * @param userID 用户的ID
     * @param callback
     */
    pressBad: (code,userID,callback) => {
        forumSchema.findOne({'code':code},'bad',(err, data) => {
            if(err||data===null) {
                let forumCode = {
                    'code': code,
                    'good': [],
                    'bad': [userID],
                    'content_id': []
                }
                newSchema = new forumSchema(forumCode)
                newSchema.save((err, doc) => {
                    callback(err, doc)
                })
            }
            else{
                badList = data['bad']
                badList.push(userID)
                forumSchema.update({'code':code},{$set: {"bad": badList}},(err,doc)=>{
                    callback(err,doc)
                })
            }

        });
    },
}