echo ">>>>> TEST GET METHOD OF JOB DECLARED SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" 'http://localhost:13111/JobDeclared?dbtype=Postgres')
echo "Result response data after GET request:"
echo "--------------------------------------------"
echo $content 
echo "--------------------------------------------"
echo "Check OK if content contains string : [Postgres.Database_Area.Tablespace_Free]"
if [[ $content =~ .*'"Postgres.Database_Area.Tablespace_Free"'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK"
	echo ">>>>> TEST POST METHOD OF JOB DECLARED SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"COMMAND":"Write","JOB":{"monitoringType":"@DB","dbType":"Postgres","jobName":"Curl.ShellScript","jobFileName":"Curl.ShellScript","jobClassName":"CLASS_A","commentForJob":"/*\nComment for job Curl.ShellScript\n*/","fetchAction":"{\nreturn \"Data output of Curl.ShellScript\"\n}","sendType":"Store","KEYEXPR":{"_chart":{}}},"PARAMS":{},"INSTANCES":{}}' 'http://localhost:13111/JobDeclared' -v)
	echo "Result response data after POST request:"
	echo "--------------------------------------------"
	echo $contentPost 
	echo "--------------------------------------------"
	echo "Check OK if content contains string : [status]"

	if [[ $contentPost =~ .*'status'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
		echo "*NOTE: CHECK IN FOLDER VAR/JOB: Curl.ShellScript.job WAS CREATED !!!"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi