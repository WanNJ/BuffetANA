let express = require('express');
let router = express.Router();

/* GET users listing. */
router.post('/search', function(req, res, next) {
    console.log(req.body);
});

router.post('/ajax/getSearchResult', function(req, res, next) {
    console.log(req.body);
    //TODO 解决搜索逻辑 关键词在query里
});

module.exports = router;