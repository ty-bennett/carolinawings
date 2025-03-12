const express = require('express');
const bodyParser = require('body-parser');

const menuRoutes = require('./routes/menu-routes');

const app = express();
app.use(menuRoutes);



app.listen(5000);