# Getting Started

I understand this solution is not enough to reach the second part of my interview, I just - out of curiolsity - would like to know on what basis you are saying that the root of my solution (of the given file) is "wrong"; since it is different than yours because of a different calculation of the rightmost leaves.


## assignement notes
There must be some kind of misunderstanding on my side about the assignment, which - running on the provided `icons_rgb_circle.png` file, generates an hash Merkle root  8613065bf7d194ec75316a8e3151e9189a5ce66a4080bd2089798bf94fc0d14a which does not match the one in the original README.md file of the task (9b39e1edb4858f7a3424d5a3d0c4579332640e58e101c29f99314a12329fc60b)
everything else matches the provided test sample. 




The server startup command is 
```
mvn spring-boot:run -Dspring-boot.run.arguments="radix-merkle-file-server-engineer-challenge/icons_rgb_circle.png"

```
with the only argument being the path of the file to serve

after start up one can issue requests such as:

```
[eddy@tycho ~]$ curl -s localhost:8080/hashes | jq
[
  {
    "pieces": 17,
    "hash": "8613065bf7d194ec75316a8e3151e9189a5ce66a4080bd2089798bf94fc0d14a"
  }
]



[eddy@tycho ~]$ curl -s localhost:8080/piece/8613065bf7d194ec75316a8e3151e9189a5ce66a4080bd2089798bf94fc0d14a/16 | jq
{
  "proof": [
    "a9dc626ab32b7af68fd4cd942804cdb94d27bdbe4feea2bb9e75635321b12766",
    "c32ef0ffc09ca674bc6217a8f2886ad73d53086c58c9019eb743db7592300a75",
    "dee5c2181d075623d4b3400f0fc2115227ecb70ec1e9fd31d874b220bcdcaa17",
    "9b0b142be514becfacf5ce54949175b8b7d68fbbeefafda72c251502bc4cd085",
    "c353eaf9fd3dd5fd12a923206258e407b6b5c575c1ae7f65c764a529e66b9d1e"
  ],
  "content": "AgDQnjDQqYLAEasBrMLihV0m+YAAALQ8CIxVQSACgRYhYLlo8Tlrbj8IAECaYWC8CgIRCFwyBvmaLX480Gt8JwgAQCZhYGJJGHBeANJnig8gAAA/hIGjVRAQBiC9ov9sVfTr6wcEAEAYAEU/IAAACAOg6AcEACD7MDCxJAw4QAyDt3iQ16x+QAAAag8DMU0oAkGnMFoU+qk7srMq+k3vAQQAYCBhYHexsCswUWgVgl5bbO2ZLgv+s5YDEACAJgaCiSWBwO4ArF085Z8utPYAAgDQwjCwdHcgfpwdgIfNLin4py0HIAAAKQWCsWWBQLsQObpUFfyLRb9efkAAALIJBONLwoBAQA4F/7S2HkAAAHg4ECy+ahmijRZbemYU/IAAALC2QDC2JAzE60GrQsNcXSz0F4t+LT2AAADQ21AwsSwU2CWgn5Y+3Z9x6y4gAAD0PxDsXhYIhAJ6WezPLCn2py0JIAAANDsULP6MFdqHeLSrSwv9wpN9QAAASCYYLIaBpcHAZWV5uVD+zFU/04WefUAAAMgyGIxVYWCi/Fm6e2AsaTtF6853VYEfrzMKfQABAGC14WCi+pdLw0G82jkYrAvV69Iif87ITQABAKDOcLAYCBYDQrEkIAgJGy/u56qfxQL/O735AAIAQBuCwmIoWBoUlv/rlFuOLlWFfLGkmF/+rz29BxAAALIODRPL/tLS3YbV/PU6TK/yryvmARrq/xdgAHvPe5F1m8tdAAAAAElFTkSuQmCCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=="
}

[eddy@tycho ~]$ curl -s localhost:8080/piece/8613065bf7d194ec75316a8e3151e9189a5ce66a4080bd2089798bf94fc0d14a/8 | jq
{
  "proof": [
    "6a10a0b8c1bd3651cba6e5604b31df595e965be137650d296c05afc1084cfe1f",
    "956bf86d100b2f49a8d057ebafa85b8db89a0f19d5627a1226fea1cb3e23d3f3",
    "04284ddea22b003e6098e7dd1a421a565380d11530a35f2e711a8dd2b9b5e7f8",
    "c66a821b749e0576e54b89dbac8f71211a508f7916e3d6235900372bed6c6c22",
    "465ab4d3f67f1a8a427fa4dbe0583ea3c3548eb6d43723837ef69604ccbb48e0"
  ],
  "content": "1wSDXYz+dPEXQP9oAYKE7Tz5ttGgCYkD3ile/OXpP4AAAPTqv+BlsRiHgknDtgQv/orRny7+AhAAgB7a+tKLxbYEp8bkJiY7bdm/L7n35ek/QN/NOQMAGYi+8c17X7AQLf8MUxOjP83+B+jzn71XLs+ZAgQZiKkxO7QCtffz27kjyYu/zP0HGAwtQJCJGA36zFtvWIgWSrWF646n/wACAFBzIfnqL7qTZGiXFC/+uvPpZ0Z/AvTfpW4AmL9yedpaQD5iKpDRoO0q/lMc/an9B2Ag5roBwDpAXuI8wLOnTlqItgSABEd/xsVf97/+xocLMCACAGQoxkkaDdqGz2m8e3YjNXc+1fsPMCDfLQ0As9YD8vLM735jNGjDpdj7Hxd/Gf0JMDAzSwOAUaCQoWdPn3QeoKHi4i+jPwGogxYgyPkPgOefK3YYDdpITyfYouXiL4CBe6AFyG3AkKl4ymw0aLPErkyK7T93P/+imL923QcMMDhagIAFMRo0Wk5ohij+Y1pTam5+OOXDBWgALUBAt9jcaTRoY6Q4oenu+S9d/AUweHNLA8C09YC8xbjJ7a93LMSARTuWi78AqMP8lcsPBACAYvuvj3VnzzM42xK8+CtGf9676KgZQFMsBoA5SwGEnSffNhp0QOJehrikLTV6/wEa4cIDAWBxOwAg2k/iUDD9t83oTwD68b/1S/71VcsBhK0vvZjkGMomi10XF38BUKO5lQKABk3gB8+89Ua3JYX+SPHpf7jj6T9AowMAwA9iNOgOrUACwEaK/08/M/oToDm+WykATFsXYKkYDRo7AdQr1Yu/tP8ANMrMSgEA4CHbXv2F0aB1B4AER3/euzhb3P/6Gx8uQHOsuAPgDACwomdPnzQatCYRrmKnJTV3PtX7D9Ak81cur7gD8J2lAVYS7SnPnjppIWqQYu9/XPxl9CdAc9kBAFYlLqhKdVLNoDz10590R66mRu8/QONcWDEAzF+5bAcAeKxnfvcbo0F76GkXfwEwAMsPAc9aEuBxYjSo8wAbF2uY4mVrdz//opi/dt0HDNAs00v/j83L/p92AViXexdniv+z76VsC7mRf/mnYuj557J4v3FgdcdbbxTX3nnPF38DUh39efPDKR8uQPM8UOMPPS4dAE8WTzu/f/NEVu95+PDLxdZDB3z4G5DieYq757908RdAM808LgDYAYB1iHnnNz86k9V73vn7t7uHWFm7CE8p7hg5/AvQWHOPCwAmAcE6RetDXH6Ui2hfifMArN22V9Mc/RmtgAA0z/yVy48NAHOWCNbv+jvvdaeg5CJGg25/veODX4OYohTrlmIABqCRHno6OfS4dACsTfQ/53Y4dvuvj3Vvs2V1thn9CQ=="
}



```

## environment
The server is a spring boot application.
This has been tested with

```
[eddy@tycho ~]$ java --version
openjdk 19 2022-09-20
OpenJDK Runtime Environment Temurin-19+36 (build 19+36)
OpenJDK 64-Bit Server VM Temurin-19+36 (build 19+36, mixed mode, sharing)

[eddy@tycho ~]$ mvn --version
Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
Maven home: /opt/maven
Java version: 19, vendor: Eclipse Adoptium, runtime: /usr/lib/jvm/java-19-temurin
Default locale: en_GB, platform encoding: UTF-8
OS name: "linux", version: "5.15.74-3-manjaro", arch: "amd64", family: "unix"


```
