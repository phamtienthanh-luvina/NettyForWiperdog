echo ">>>>> TEST GET METHOD OF DBCOMMONCONFIG SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" 'http://localhost:13111/DBCommonConfig?dbtype=MySQL&jobName=MySQL.Database_Area.Top30Database')
echo "Result response data after GET request:"
echo "--------------------------------------------"
echo $content 
echo "--------------------------------------------"
echo "Check OK if content contains string : [jobName": "MySQL.Database_Area.Top30Database]"
if [[ $content =~ .*'"jobName": "MySQL.Database_Area.Top30Database"'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK"
	echo ">>>>> TEST POST METHOD OF DBCOMMONCONFIG SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"job":"MySQL.Database_Area.Top30Database","data":{"params":{"dbHostId":"a","dbSid":"a"},"instances":{"inst_1":{"schedule":"60i","params":{"dbHostId":"b","dbSid":"b"}}},"jobName":"MySQL.Database_Area.Top30Database"}}' http://localhost:13111/DBCommonConfig -v)
	echo "Result response data after POST request:"
	echo "--------------------------------------------"
	echo $contentPost 
	echo "--------------------------------------------"
	echo "Check OK if content contains string : [OK]"
	if [[ $contentPost =~ .*'OK'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
		echo "*NOTE: CHECK IN FOLDER VAR/JOB: MySQL.Database_Area.Top30Database.instances AND MySQL.Database_Area.Top30Database.params WERE CREATED !!!"
	else
		echo "[MESSAGE TEST] POST IS FAILURE"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi