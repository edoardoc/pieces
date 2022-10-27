# Radix Engineer Challenge - Merkle File Server

This programming challenge is meant to assess your ability to translate ideas and specifications into code, while dealing with complex problems that may be unfamiliar.

We appreciate your confidentiality with regards to the instructions of this challenge.

## Your task

Please create your solution as a private repo in github.

Your task is to implement a proof of concept Trustless File Server according to the specifications below.
Please program your server in your choice of Rust, Java or C#.

Please provide a `README.md` detailing how your server should be run.

You will be assessed based on the readability, as well as implementation and design decisions of your code.

### Pointers

* You may make use of any libraries/frameworks you wish (eg a standard web server in your language).
* We will not test your server with files that exceed 4MB, so you can store everything in memory.
* At Radix, we trust engineers to suggest designs and suggest tweaks/improvements to designs. If you happen to have any thoughts or suggestions related to the specification, please provide these in your README.

### Submission

In your github repo, under Settings > Collaborators, add https://github.com/dhedey, https://github.com/LukasGasior1, https://github.com/siy and https://github.com/talekhinezh as read-only collaborators. Let us know when you’re ready to have your solution reviewed.

# Product Goals / Context

_The following is written as a hypothetical brief for the file server product_

* We need to enable users to download large files in a decentralized manner
* We'd like you to create a proof of concept server, that we can play with and test before considering how to productionise it
* The proof of concept is intended to show that a file can be served to a user in a manner which is fault-tolerant and tamper-safe
* The user should be able to reconstruct the file, and verify the integrity of the file chunk-by-chunk against a small digest they have received from a trusted source


# Product Overview

You are tasked to write a basic trustless file server, similar in concept to the BitTorrent peer-to-peer software.

In this scheme, a client who wishes to download a file would go to a trusted source to find the root merkle hash and file size. Using these values, the client can then request file pieces from an untrusted source (or sources) running your server, and receive a merkle proof alongside the file pieces that they can use to verify that the received file pieces are correct.

The server should expose a `hashes` HTTP endpoint to serve as something to represent this trusted source.

The server should also expose a `piece` HTTP endpoint can be used to download each piece of the file, along with a *proof* for the piece which they can use to reconstruct the root hash and verify that the provided piece is correct. By doing this for all the pieces, they can reconstruct the whole file.

## Merkle trees

A merkle tree is a hash-based tree data structure used in distributed systems for efficient data verification. They are used in peer-to-peer networks like Tor, Bitcoin, Git, and BitTorrent.

You can find more detailed interview information here: [https://brilliant.org/wiki/merkle-tree/](https://brilliant.org/wiki/merkle-tree/).

## Our file merkle tree specification

The algorithm we will use to calculate the merkle hash will follow closely the BitTorrent method of calculating a file's hash as seen in [Simple Merkle Hashes here](http://www.bittorrent.org/beps/bep_0030.html), except we will use the SHA-256 algorithm rather than SHA1, and we will have a hardcoded piece size of 1KB.

In particular:

* The merkle tree will be a perfectly balanced binary tree.
  * For leaf node hashes corresponding to complete file pieces, we take `Node Hash = SHA256(the 1024 file piece bytes)`.
  * For leaf node hashes corresponding to incomplete file pieces, we pad the chunk with 0s to a length of 1024 bytes before taking the SHA256 hash, ie `Node Hash = SHA256(the 1024 padded file piece bytes)`.
  * For leaf node hashes corresponding to non-existent file pieces (eg off the end of the file), we use a filler hash value of 32 zero bytes, ie `Node Hash = 0000...00`.
* Hash values for nodes in the higher levels of the tree are calculated by concatenating the child hash bytes of the left child with the child hash bytes of the right child, left to right, and computing the hash of that 64-byte aggregate.

## Instructions

Write a simple web application which can serve the contents of a single file in pieces of 1KB (1024 bytes) along with proof that the piece has the correct data.

We will start your server with a single argument: the path to the single local file you will serve through your web server.

Something similar to this:
`$ java com.radix.challenge.TrustlessFileServer icons_rgb_circle.png`

Your server must then expose two HTTP endpoints:
1. `GET /hashes`
2. `GET /piece/:hashId/:pieceIndex`


## Endpoint Specification

*The example responses below are based on the file `icons_rgb_circle.png` provided with the specification as a test case*

### GET /hashes

This endpoint should return a json list of the merkle hashes and number of 1KB pieces of the files this server is serving. In our case this will be a singleton array.

Example:
```sh
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/hashes
```

```json
[{
  "hash": "9b39e1edb4858f7a3424d5a3d0c4579332640e58e101c29f99314a12329fc60b",
  "pieces": 17
}]
```

### GET /piece/:hashId/:pieceIndex

This endpoint should return a verifiable piece of the content.

Parameter   | Description
----------- | -------------
:hashId     | the merkle hash of the file we want to download (in our case there will only be one)
:pieceIndex | the index of the piece we want to download (from zero to 'filesize divided by 1KB')

The returned object will contain two fields:

Field   | Description
------- | -------------
content | The binary content of the piece encoded in base64.
proof   | A list of hashes hex encoded to prove that the piece is legitimate. The first hash will be the hash of the sibling and the next will be the uncle's hash, the next the uncle of the uncle's hash and so on. With this information the client will be able to recalculate the root hash of the tree and compare it to the known root hash. (Please see [Simple Merkle Hashes](http://www.bittorrent.org/beps/bep_0030.html) for a more thorough discussion)

Example:
```sh
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/piece/9b39e1edb4858f7a3424d5a3d0c4579332640e58e101c29f99314a12329fc60b/8
```

```json
{
  "content": "1wSDXYz+dPEXQP9oAYKE7Tz5ttGgCYkD3ile/OXpP4AAAPTqv+BlsRiHgknDtgQv/orRny7+AhAAgB7a+tKLxbYEp8bkJiY7bdm/L7n35ek/QN/NOQMAGYi+8c17X7AQLf8MUxOjP83+B+jzn71XLs+ZAgQZiKkxO7QCtffz27kjyYu/zP0HGAwtQJCJGA36zFtvWIgWSrWF646n/wACAFBzIfnqL7qTZGiXFC/+uvPpZ0Z/AvTfpW4AmL9yedpaQD5iKpDRoO0q/lMc/an9B2Ag5roBwDpAXuI8wLOnTlqItgSABEd/xsVf97/+xocLMCACAGQoxkkaDdqGz2m8e3YjNXc+1fsPMCDfLQ0As9YD8vLM735jNGjDpdj7Hxd/Gf0JMDAzSwOAUaCQoWdPn3QeoKHi4i+jPwGogxYgyPkPgOefK3YYDdpITyfYouXiL4CBe6AFyG3AkKl4ymw0aLPErkyK7T93P/+imL923QcMMDhagIAFMRo0Wk5ohij+Y1pTam5+OOXDBWgALUBAt9jcaTRoY6Q4oenu+S9d/AUweHNLA8C09YC8xbjJ7a93LMSARTuWi78AqMP8lcsPBACAYvuvj3VnzzM42xK8+CtGf9676KgZQFMsBoA5SwGEnSffNhp0QOJehrikLTV6/wEa4cIDAWBxOwAg2k/iUDD9t83oTwD68b/1S/71VcsBhK0vvZjkGMomi10XF38BUKO5lQKABk3gB8+89Ua3JYX+SPHpf7jj6T9AowMAwA9iNOgOrUACwEaK/08/M/oToDm+WykATFsXYKkYDRo7AdQr1Yu/tP8ANMrMSgEA4CHbXv2F0aB1B4AER3/euzhb3P/6Gx8uQHOsuAPgDACwomdPnzQatCYRrmKnJTV3PtX7D9Ak81cur7gD8J2lAVYS7SnPnjppIWqQYu9/XPxl9CdAc9kBAFYlLqhKdVLNoDz10590R66mRu8/QONcWDEAzF+5bAcAeKxnfvcbo0F76GkXfwEwAMsPAc9aEuBxYjSo8wAbF2uY4mVrdz//opi/dt0HDNAs00v/j83L/p92AViXexdniv+z76VsC7mRf/mnYuj557J4v3FgdcdbbxTX3nnPF38DUh39efPDKR8uQPM8UOMPPS4dAE8WTzu/f/NEVu95+PDLxdZDB3z4G5DieYq757908RdAM808LgDYAYB1iHnnNz86k9V73vn7t7uHWFm7CE8p7hg5/AvQWHOPCwAmAcE6RetDXH6Ui2hfifMArN22V9Mc/RmtgAA0z/yVy48NAHOWCNbv+jvvdaeg5CJGg25/veODX4OYohTrlmIABqCRHno6OfS4dACsTfQ/53Y4dvuvj3Vvs2V1thn9CQ==",
  "proof": [
    "6a10a0b8c1bd3651cba6e5604b31df595e965be137650d296c05afc1084cfe1f", // sibling hash
    "956bf86d100b2f49a8d057ebafa85b8db89a0f19d5627a1226fea1cb3e23d3f3", // uncle hash
    "04284ddea22b003e6098e7dd1a421a565380d11530a35f2e711a8dd2b9b5e7f8", // uncle's uncle hash
    "c66a821b749e0576e54b89dbac8f71211a508f7916e3d6235900372bed6c6c22", // etc.
    "a8bd48117723dee92524c25730f9e08e5d47e78c87d17edb344d4070389d049e"  // child of root
  ]
}
```

