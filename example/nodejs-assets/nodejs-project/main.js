/* eslint-disable no-undef */

var Client = require('@gr4vy/node').Client
var minimist = require('minimist')
var rn_bridge = require('rn-bridge')

const { gr4vyId = 'spider', privateKey } = minimist(process.argv.slice(2)) || {}

let client
try {
  client = new Client({
    gr4vyId,
    privateKey: Buffer.from(privateKey, 'base64').toString(),
  })
} catch (err) {
  rn_bridge.channel.send({
    type: 'log',
    data: { message: 'Error creating the client' },
  })
}

// Listen to messages received from react-native.
rn_bridge.channel.on('message', async ({ type, data }) => {
  if (client && type === 'token' && data) {
    const token = await client.getEmbedToken(data)
    rn_bridge.channel.send({ type: 'token', data: token })
  }
})
