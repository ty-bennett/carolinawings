const express = require('express');

const router = express.Router();

router.get('/menu', (req, res, next) => {
  console.log('GET Request in MENU');
  res.json({message: 'It works'});
});

module.exports = router;