/**
 * Created by slow_time on 2017/5/4.
 */
let userDB = require('../models/user.js').userDB;

exports.login = (username, password, callback) => {
  userDB.getPasswordByName(username, (err, doc) => {
      if (doc === null)
          callback(err, null);
      else if (doc["password"] !== password)
          callback(err, false);
      else
          callback(err, true);
  });
};

exports.signUp = (username, password, email, callback) => {
    userDB.getPasswordByName(username, (err, doc) => {
        if (doc === null) {
            let user = {
                username : username,
                password : password,
                email : email
            };
            userDB.registerUser(user, (err, data) => {
                callback(err, true);
            });
        }
        else
            callback(err, false);
    });
};