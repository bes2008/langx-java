#!/bin/bash


function encrypt() {
        message=$1
        passphrase=$2

        # 生成 salt
        salt=`openssl rand -hex 8`
        # 生成 secret key, iv
        derivedKey=`openssl enc -S $salt -k ${passphrase} -md md5 -aes-256-cbc -P`
        echo $derivedKey

        echo '生成的key信息(hex格式):'

        salt=`echo $derivedKey | grep -o -E 'salt(\s)*=(\s)*\w*' | grep -o -E '[A-Z0-9]+' `
        echo salt: "$salt"

        secretKey=`echo $derivedKey | grep -o -E 'key(\s)*=(\s)*\w*' | grep -o -E '[A-Z0-9]+' `
        echo secret key: "$secretKey"

        iv=`echo $derivedKey | grep -o -E 'iv(\s)*=(\s)*\w*' | grep -o -E '[A-Z0-9]+' `
        echo iv: "$iv"

        # 加密
        ciphertext=`echo -n $message | openssl enc -K $secretKey -iv $iv -e -aes-256-cbc -base64`
        echo '加密后数据(base64格式):' "$ciphertext"
        ciphertextHex=`echo -n $ciphertext | base64 -d | xxd -p`
        echo '加密后数据(   hex格式):' "$ciphertextHex"

        # 加盐
        saltPrefix="Salted__"

        saltPrefixHex=`echo -n $saltPrefix | xxd -p`
        echo 'salt prefix(hex格式):' $saltPrefixHex

        saltedTextHex="$saltPrefixHex$salt$ciphertextHex"
        saltedTextBase64=`echo -n $saltedTextHex | xxd -r -p | base64`

        echo '加盐后数据(   hex格式):' "$saltedTextHex"
        echo '加盐后数据(base64格式):' "$saltedTextBase64"


}

encrypt 'test@123' 'NsFoCus$#'
