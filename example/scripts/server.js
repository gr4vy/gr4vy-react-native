const Gr4vy = require('@gr4vy/sdk')
const { getEmbedToken } = Gr4vy
const http = require('node:http')

require('dotenv').config({
  path: require('path').join(__dirname, '..', '.env'),
})

const privateKey = Buffer.from(
  process.env.GR4VY_PRIVATE_KEY,
  'base64'
).toString()

const parseBody = (req, res, next) => {
  let data = ''
  req.body = {}

  req.on('data', (chunk) => {
    data += chunk
  })

  req.on('end', () => {
    try {
      req.body = JSON.parse(data)
    } catch (e) {
      req.body = data
    }
    next(req, res)
  })
}

const server = http.createServer(async (req, res) => {
  if (req.method === 'POST' && req.url === '/token') {
    return parseBody(req, res, async (req, res) => {
      try {
        console.log(privateKey, process.env.GR4VY_ID, req.body)
        const token = await getEmbedToken({
          privateKey,
          embedParams: {
            gr4vyId: process.env.GR4VY_ID,
            ...req.body,
          },
        })

        const json = JSON.stringify({ token })

        res.writeHead(200, {
          'Content-Type': 'application/json',
          'Content-Length': Buffer.byteLength(json),
        })
        res.end(json)
      } catch (error) {
        res.statusCode = 500
        res.setHeader('Content-Type', 'application/json')
        res.end(JSON.stringify({ error }))
      }
    })
  }
})

server.listen(9010, () => {
  console.log('Dev server listening on http://localhost:9010')
})
