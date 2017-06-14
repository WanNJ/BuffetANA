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
        contentSchema.findOne({'_id':c_id},['good', 'code'],(err, data) => {
            let goodList = data['good'];
            goodList.push(userID);
            let stockCode = data["code"];
            contentSchema.update({'_id':c_id},{$set: {"good": goodList}},(err)=>{
                callback(err, stockCode);
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
         contentSchema.findOne({'_id': c_id}, ['bad', 'code'], (err, data) => {
             let goodList = data['bad'];
             goodList.push(userID);
             let stockCode = data["code"];
             contentSchema.update({'_id': c_id}, {$set: {"bad": goodList}}, (err) => {
                 callback(err, stockCode);
             })
         });

    },

    getAllContent:(code,userID,callback)=>{
        contentSchema.find({'code': code},null , {sort:{'date':'desc'}} , (err, data) => {
            if(data.length===0){
                callback(err,[])
            }
            else{

                function contains(arr, obj) {
                    var i = arr.length;
                    while (i--) {
                        if (arr[i] === obj) {
                            return true;
                        }
                    }
                    return false;
                }

                result = []
                for(let i = 0 ; i< data.length ; i++){
                    d = data[i];
                    let temp ={
                        "like":d['good'].length,
                        "dislike":d['bad'].length,
                        "date":d['date'] ,
                        "clickAble": !(contains(d['good'],userID) || contains(d['bad'],userID)),
                        "content": d['content'],
                        "username":d['userID'],
                        "_id" :d['_id']
                    }
                    result.push(temp)

                }
                callback(err,result)
            }

        });
    },

    /**
     * 通过帖子的ID  获得是谁的帖子
     * @param callback
     */
    getSenderByID: (content_id , callback) => {
        contentSchema.find({_id: content_id},"userID",(err, doc) => {
            callback(err, doc[0]['userID'])
        });
    },

}