const express = require('express');
const cors = require('cors');
const app = express();
const PORT = 3000;

app.use(cors());

let cachedResponse = null;
let cacheTimestamp = null;
const CACHE_DURATION = 60000; // 1 minute in milliseconds

app.get('/api', (req, res) => {
  const now = Date.now();
  
  if (cachedResponse && cacheTimestamp && (now - cacheTimestamp) < CACHE_DURATION) {
    res.json(cachedResponse);
  } else {
    const randomNumber = Math.floor(Math.random() * 1000);
    cachedResponse = { number: randomNumber };
    cacheTimestamp = now;
    res.json(cachedResponse);
  }
});

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
