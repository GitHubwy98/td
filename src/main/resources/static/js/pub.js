var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQkkaLxLPi01pGxuZmHwYNGuhCsMdZsvz43TJ8VQv1vbBBIfqV6j+3Lgf0puJ6ogHWNH3ijeYBye+1SQ97fV1qAXshzhmcExS/qsHwpNqAjlb/LCDPVXCsAPO0jjrt8rXAMdgFX8TA3OaljcTOZ3eswfdV2QvkmFYRTIfcoclGRQIDAQAB";
if (publicKey == null) {
    $.ajax({
        url:"/toObtain",
        type: "get",
        dataType: "json",
        success:function (data) {
            if (data){
                publicKey=data.data;
                console.log(publicKey)
            }
        }
    });
}
$.ajaxSetup({


});

function encryptionToDecrypt(data) {
    if (publicKey==null){
        return
    }
    var encrypt = new JSEncrypt();
    encrypt.default_key_size=1024;
    encrypt.setPublicKey(publicKey);
    var encrypted = encrypt.encrypt(data);
    console.log(encrypted)
    return encrypted;
}