#!/bin/bash
#  build.sh
#
#  Created by LanBiao 11/2/16.
#  Copyright (c) 2016 LanBiao. All rights reserved.

while getopts "rdlufm" opt; do
    case $opt in
        r) recover="YES";;
        d) dev="YES";;
        l) local="YES";;
        m) remove_old_link="YES";;
        u) update_old_html="YES";;
        f) fangzhen="YES";;
        #a) args=$OPTARG;;
        ?) echo unspport args; exit 1;;
    esac
done


ftp_account="fnputuser"
ftp_passwd="Efn3#892n18@719L\r"
ftp_adress='120.26.122.214'
apk_adress='https://w.fengnian.cn/Wallet/download/Android/smallYellowO'
ipa_port=''
connect_port='21561'
upload_adress='/mydatas/wallet_svn/html5/download'


initDir=$(pwd)
cd ..
apkOutPutPath="$(pwd)/build/outputs/apk"
cd ..


apkName="smallYellowO"
if [[ $fangzhen == "YES" || $dev == "YES" ]];then
htmlFile=${apkName}Test.html
else
htmlFile=${apkName}.html
fi

apkBackupPath="${HOME}/workspace/Android/${apkName}"

mkdir -p $apkBackupPath

plistPlist=${apkName}.plist

versionName=$(cat app/build.gradle|grep versionName|cut -d '"' -f2)
#versionName=$(cat app/build.gradle|grep versionName|awk '{print $2}')
buildNo=$(svn info|grep Revision|awk '{print $2}')

if [[ $update_old_html == "YES" ]];then

expect <<DONE
set timeout 300
spawn scp -o StrictHostKeyChecking=no -P $connect_port ${ftp_account}@${ftp_adress}:${upload_adress}/${htmlFile} ${initDir}/${htmlFile}
expect "password:"
send $ftp_passwd
expect "]*"
exit
interact
DONE


expect <<DONE
set timeout 300
spawn scp -o StrictHostKeyChecking=no -P $connect_port ${ftp_account}@${ftp_adress}:${upload_adress}/Android/smallYellowO/smallYellowO.plist ${initDir}/${plistPlist}
expect "password:"
send $ftp_passwd
expect "]*"
exit
interact
DONE

fi


if [[ $remove_old_link == "YES" ]];then
sed -i "" "/<h1><a href=\"http.*\.apk/d" ${initDir}/${htmlFile}
fi

gradle clean

PlistBuddy="/usr/libexec/PlistBuddy"

if [[ $dev == "YES" ]];then

testversion=$($PlistBuddy -c "Print :items:0:assets:0:versionname" ${initDir}/$plistPlist)
if [[ $testversion != $versionName ]];then
testversioncount=1
$PlistBuddy -c "set :items:0:assets:0:testversioncount ${testversioncount}" ${initDir}/$plistPlist
$PlistBuddy -c "set :items:0:assets:0:versionname ${versionName}" ${initDir}/$plistPlist
apk_prefix=${apkName}_${versionName}_${testversioncount}
else
testversioncount=$($PlistBuddy -c "Print :items:0:assets:0:testversioncount" ${initDir}/$plistPlist)
let testversioncount++;
$PlistBuddy -c "set :items:0:assets:0:testversioncount ${testversioncount}" ${initDir}/$plistPlist
apk_prefix=${apkName}_${versionName}_${testversioncount}
fi

elif [[ $fangzhen == "YES" ]];then

testversion=$($PlistBuddy -c "Print :items:0:assets:0:versionname" ${initDir}/$plistPlist)
if [[ $testversion != $versionName ]];then
testversioncount=1
$PlistBuddy -c "set :items:0:assets:0:testversioncount ${testversioncount}" ${initDir}/$plistPlist
$PlistBuddy -c "set :items:0:assets:0:versionname ${versionName}" ${initDir}/$plistPlist
apk_prefix=${apkName}_${versionName}_${testversioncount}
else
testversioncount=$($PlistBuddy -c "Print :items:0:assets:0:testversioncount" ${initDir}/$plistPlist)
let testversioncount++;
$PlistBuddy -c "set :items:0:assets:0:testversioncount ${testversioncount}" ${initDir}/$plistPlist
apk_prefix=${apkName}_${versionName}_${testversioncount}
fi

else
apk_prefix=${apkName}_${versionName}_${buildNo}
versionCode=$(cat app/build.gradle|grep versionCode|awk '{print $2}')
let versionCode++
sed -i "" "/versionCode.*/d" app/build.gradle
sed -i "" "/targetSdkVersion.*/a\\
versionCode $versionCode
" app/build.gradle
fi


if [[ $recover != "YES" ]] && [[ -e ${apkBackupPath}/${apk_prefix}.apk ]];then
    i=1
    while [[ -e ${apkBackupPath}/${apk_prefix}_$i.apk ]];do
        let i++
    done
    apk_prefix="${apk_prefix}($i)"
fi

downloadLinkLine=$(grep ${apk_prefix} ${initDir}/${htmlFile})

oldBuildTypes=$(cat app/build.gradle|grep release{buildConfigField)
sed -i "" "/release{buildConfigField.*/d" app/build.gradle

if [[ $dev == "YES" ]];then

if [[ $downloadLinkLine == '' ]];then
    sed -i "" "/<h1>Android*/a\\
<h1><a href=\"${apk_adress}/${apk_prefix}.apk\">点击下载小黄圈Android开发环境版本${apk_prefix}</a></h1>\\
" ${initDir}/${htmlFile}
fi


sed -i "" "/buildTypes {.*/a\\
release{buildConfigField \"Integer\", \"BUILD_TYPE_DEFINE\",\"2002\"\\
" app/build.gradle



cd $initDir/../src/
#友盟相关
manifestpath=$(find ./ -name AndroidManifest.xml)
oldUMengAppKey=$(cat $manifestpath | grep "android:name=\"UMENG_APPKEY\" android:value=")
sed -i "" "/android:name=\"UMENG_APPKEY\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
<meta-data android:name=\"UMENG_APPKEY\" android:value=\"582d69a3a40fa375ba00019b\" />\\
" $manifestpath

oldUMengAppChannel=$(cat $manifestpath | grep "android:name=\"UMENG_CHANNEL\" android:value=")
sed -i "" "/android:name=\"UMENG_CHANNEL\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
<meta-data android:name=\"UMENG_CHANNEL\" android:value=\"cn_guangwang\" />\\
" $manifestpath
echo $oldUMengAppKey
echo $oldUMengAppChannel

#个推相关
oldGeTuiAppKey=$(cat $manifestpath | grep "<action android:name=\"com.igexin.sdk.action")
sed -i "" "/<action android:name=\"com.igexin.sdk.action/d" $manifestpath
echo $oldGeTuiAppKey
sed -i "" "/android:name=\".receivers.GeTuiReceiver\"/a\\
<intent-filter><action android:name=\"com.igexin.sdk.action.EtvOACOJjq9VGI4rx1Ktd1\" /></intent-filter>\\
" $manifestpath

cd ../../
oldGeTuiCofig=$(cat app/build.gradle | grep "GETUI_APP_ID :")
echo $oldGeTuiCofig
sed -i "" "/GETUI_APP_ID :/d" app/build.gradle
sed -i "" "/manifestPlaceholders =/a\\
GETUI_APP_ID : \"EtvOACOJjq9VGI4rx1Ktd1\", GETUI_APP_KEY : \"aGLWgI5jUg7pCEEogxVHY3\", GETUI_APP_SECRET : \"13uxdrHzUJ8wokJSyfnIK3\",\\
" app/build.gradle


FFConfigPath=$(find ./ -name FFConfig.java)
oldPrintLog=$(cat $FFConfigPath | grep "public static final boolean IS_OFFICIAL")
echo $oldPrintLog
sed -i "" "/public static final boolean IS_OFFICIAL/d" $FFConfigPath
sed -i "" "/是否正式发布版本/a\\
public static final boolean IS_OFFICIAL = false;\\
" $FFConfigPath





elif [[ $fangzhen == "YES" ]];then

if [[ $downloadLinkLine == '' ]];then
sed -i "" "/<h1>Android*/a\\
<h1><a href=\"${apk_adress}/${apk_prefix}.apk\">点击下载小黄圈Android仿真环境版本${apk_prefix}</a></h1>\\
" ${initDir}/${htmlFile}
fi


sed -i "" "/buildTypes {.*/a\\
release{buildConfigField \"Integer\", \"BUILD_TYPE_DEFINE\",\"2003\"\\
" app/build.gradle


cd $initDir/../src/
#友盟相关
manifestpath=$(find ./ -name AndroidManifest.xml)
oldUMengAppKey=$(cat $manifestpath | grep "android:name=\"UMENG_APPKEY\" android:value=")
sed -i "" "/android:name=\"UMENG_APPKEY\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
<meta-data android:name=\"UMENG_APPKEY\" android:value=\"582d69a3a40fa375ba00019b\" />\\
" $manifestpath

oldUMengAppChannel=$(cat $manifestpath | grep "android:name=\"UMENG_CHANNEL\" android:value=")
sed -i "" "/android:name=\"UMENG_CHANNEL\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
<meta-data android:name=\"UMENG_CHANNEL\" android:value=\"cn_guangwang\" />\\
" $manifestpath
echo $oldUMengAppKey
echo $oldUMengAppChannel

#个推相关
oldGeTuiAppKey=$(cat $manifestpath | grep "<action android:name=\"com.igexin.sdk.action")
sed -i "" "/<action android:name=\"com.igexin.sdk.action/d" $manifestpath
echo $oldGeTuiAppKey
sed -i "" "/android:name=\".receivers.GeTuiReceiver\"/a\\
<intent-filter><action android:name=\"com.igexin.sdk.action.EtvOACOJjq9VGI4rx1Ktd1\" /></intent-filter>\\
" $manifestpath

cd ../../
oldGeTuiCofig=$(cat app/build.gradle | grep "GETUI_APP_ID :")
echo $oldGeTuiCofig
sed -i "" "/GETUI_APP_ID :/d" app/build.gradle
sed -i "" "/manifestPlaceholders =/a\\
GETUI_APP_ID : \"EtvOACOJjq9VGI4rx1Ktd1\", GETUI_APP_KEY : \"aGLWgI5jUg7pCEEogxVHY3\", GETUI_APP_SECRET : \"13uxdrHzUJ8wokJSyfnIK3\",\\
" app/build.gradle


FFConfigPath=$(find ./ -name FFConfig.java)
oldPrintLog=$(cat $FFConfigPath | grep "public static final boolean IS_OFFICIAL")
echo $oldPrintLog
sed -i "" "/public static final boolean IS_OFFICIAL/d" $FFConfigPath
sed -i "" "/是否正式发布版本/a\\
public static final boolean IS_OFFICIAL = false;\\
" $FFConfigPath



else

if [[ $downloadLinkLine == '' ]];then
sed -i "" "/<h1>Android*/a\\
<h1><a href=\"${apk_adress}/${apk_prefix}.apk\">点击下载小黄圈Android正式环境版本${apk_prefix}</a></h1>\\
" ${initDir}/${htmlFile}
fi

sed -i "" "/buildTypes {.*/a\\
release{buildConfigField \"Integer\", \"BUILD_TYPE_DEFINE\",\"2004\"\\
" app/build.gradle


cd $initDir/../src/
#友盟相关
manifestpath=$(find ./ -name AndroidManifest.xml)
oldUMengAppKey=$(cat $manifestpath | grep "android:name=\"UMENG_APPKEY\" android:value=")
sed -i "" "/android:name=\"UMENG_APPKEY\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
<meta-data android:name=\"UMENG_APPKEY\" android:value=\"57ccd766e0f55ada5500067c\" />\\
" $manifestpath

oldUMengAppChannel=$(cat $manifestpath | grep "android:name=\"UMENG_CHANNEL\" android:value=")
sed -i "" "/android:name=\"UMENG_CHANNEL\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
<meta-data android:name=\"UMENG_CHANNEL\" android:value=\"cn_guangwang\" />\\
" $manifestpath
echo $oldUMengAppKey
echo $oldUMengAppChannel

#个推相关
oldGeTuiAppKey=$(cat $manifestpath | grep "<action android:name=\"com.igexin.sdk.action")
sed -i "" "/<action android:name=\"com.igexin.sdk.action/d" $manifestpath
echo $oldGeTuiAppKey
sed -i "" "/android:name=\".receivers.GeTuiReceiver\"/a\\
<intent-filter><action android:name=\"com.igexin.sdk.action.kh8zmnbbig90lrw8apeuj4\" /></intent-filter>\\
" $manifestpath

cd ../../
oldGeTuiCofig=$(cat app/build.gradle | grep "GETUI_APP_ID :")
echo $oldGeTuiCofig
sed -i "" "/GETUI_APP_ID :/d" app/build.gradle
sed -i "" "/manifestPlaceholders =/a\\
GETUI_APP_ID : \"kh8zmnbbig90lrw8apeuj4\", GETUI_APP_KEY : \"EBTpWy6UCYAZkosdVG50h5\", GETUI_APP_SECRET : \"IuWIOrhVFj71Rwqy4raQQ7\",\\
" app/build.gradle


FFConfigPath=$(find ./ -name FFConfig.java)
oldPrintLog=$(cat $FFConfigPath | grep "public static final boolean IS_OFFICIAL")
echo $oldPrintLog
sed -i "" "/public static final boolean IS_OFFICIAL/d" $FFConfigPath
sed -i "" "/是否正式发布版本/a\\
public static final boolean IS_OFFICIAL = true;\\
" $FFConfigPath


fi


gradle build

cp ${apkOutPutPath}/app-release.apk ${apkBackupPath}/${apk_prefix}.apk

if [[ $local != "YES" ]];then

expect <<DONE
set timeout 300
spawn scp -o StrictHostKeyChecking=no -P $connect_port ${apkBackupPath}/${apk_prefix}.apk ${ftp_account}@$ftp_adress:${upload_adress}/Android/smallYellowO/
expect "password:"
send $ftp_passwd
expect "]*"
exit
interact
DONE

expect <<DONE
set timeout 300
spawn scp -o StrictHostKeyChecking=no -P $connect_port ${initDir}/${plistPlist} ${ftp_account}@$ftp_adress:${upload_adress}/Android/smallYellowO/
expect "password:"
send $ftp_passwd
expect "]*"
exit
interact
DONE


expect <<DONE
set timeout 300
spawn scp -o StrictHostKeyChecking=no -P $connect_port ${initDir}/${htmlFile} ${ftp_account}@${ftp_adress}:${upload_adress}/
expect "password:"
send $ftp_passwd
expect "]*"
exit
interact
DONE

fi


sed -i "" "/release{buildConfigField.*/d" app/build.gradle
sed -i "" "/buildTypes {.*/a\\
${oldBuildTypes}\\
" app/build.gradle


cd $initDir/../src/
#友盟相关
manifestpath=$(find ./ -name AndroidManifest.xml)

sed -i "" "/android:name=\"UMENG_APPKEY\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
${oldUMengAppKey}\\
" $manifestpath

sed -i "" "/android:name=\"UMENG_CHANNEL\" android:value=/d" $manifestpath
sed -i "" "/<!--umeng-->/a\\
${oldUMengAppChannel}\\
" $manifestpath

sed -i "" "/<action android:name=\"com.igexin.sdk.action/d" $manifestpath
sed -i "" "/android:name=\".receivers.GeTuiReceiver\"/a\\
${oldGeTuiAppKey}\\
" $manifestpath

cd ../../

sed -i "" "/GETUI_APP_ID :/d" app/build.gradle
sed -i "" "/manifestPlaceholders =/a\\
${oldGeTuiCofig}\\
" app/build.gradle


FFConfigPath=$(find ./ -name FFConfig.java)
sed -i "" "/public static final boolean IS_OFFICIAL/d" $FFConfigPath
sed -i "" "/是否正式发布版本/a\\
${oldPrintLog}\\
" $FFConfigPath


echo success!

