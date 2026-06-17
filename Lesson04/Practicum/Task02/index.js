const express = require('express')
const cors = require('cors')
const path = require('path')
const app = express()
const port = 3000

app.use(cors({
    origin: '*'
}))

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'login.html'))
})

app.get('/dashboard', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'dashboard.html'))
})


app.use(express.json())

app.post('/api/login', (req, res) => {
    const { username, password } = req.body
    if (username === 'admin' && password === 'admin123') {
        res.json({ status: 'success' })
    } else {
        res.json({ status: 'fail' })
    }
})

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})