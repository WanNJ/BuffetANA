/**
 * Created by wshwbluebird on 2017/6/8.
 */
let contentSchema = require('./contentSchema');

exports.contentDB = {
    /**
     *
     * @param code
     * @param userID
     * @param content
     * @param callback
     */
    addContent: (code, userID, content, callback) => {
        let mContent = {
            'code':code,
            'userID':userID,
            'content':content,
            'good':[],
            'bad':[],
            'date':Date.now()
        }
        let newContent = new contentSchema(mContent)
        newContent.save((err, data) => {
            callback(err,data)
        });
    },

    /**
     * 点赞／看好
     * @param c_id  评论的ID
     * @param userID 用户的ID
     * @param callback
     */
    pressGood :(c_id,userID,callback)=>{
        contentSchema.findOne({'_id':c_id},'good',(err, data) => {
            let goodList = data['good'];
            goodList.push(userID);
            contentSchema.update({'_id':c_id},{$set: {"good": goodList}},(err,doc)=>{
                callback(err,doc)
            })
        });
    },

    /**
     * 点赞／看好
     * @param c_id  评论的ID
     * @param userID 用户的ID
     * @param callback
     */
    pressBad: (c_id,userID,callback) => {
        forumSchema.findOne({'code':code},'bad',(err, data) => {
            contentSchema.findOne({'_id': c_id}, 'bad', (err, data) => {
                let goodList = data['bad'];
                goodList.push(userID);
                contentSchema.update({'_id': c_id}, {$set: {"good": goodList}}, (err, doc) => {
                    callback(err, doc)
                })
            });
        })
    },

}