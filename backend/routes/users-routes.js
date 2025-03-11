const express = require('express');

const router = express.Router();

router.get('/api/users/', (req, res, next) => {
  console.log('GET Request in users');
  res.json({message: 'It works'});
});

module.exports = router;