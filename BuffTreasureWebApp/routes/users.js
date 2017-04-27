var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/:id', function(req, res, next) {
    console.log('Request URL:', req.originalUrl);
    res.send('User requests to get: ' + req.params.id);
});

module.exports = router;
